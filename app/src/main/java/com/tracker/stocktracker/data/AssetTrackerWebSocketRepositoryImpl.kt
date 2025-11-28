package com.tracker.stocktracker.data

import com.tracker.stocktracker.model.PriceInfoDataModel
import com.tracker.network.NetworkConstants.BASE_WEB_SOCKET_URL
import com.tracker.network.NetworkConstants.WEB_SOCKET_URL_RAW
import com.tracker.stocktracker.domain.AssetTrackerWebSocketRepository
import com.tracker.stocktracker.utils.*
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * This class help to connect to WebSocket for duplex bidirectional communication asynchronously
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
class AssetTrackerWebSocketRepositoryImpl(
    private val client: HttpClient
) : AssetTrackerWebSocketRepository {

    private val _prices = MutableStateFlow<Map<String, PriceInfoDataModel>>(emptyMap())
    override val prices = _prices.asStateFlow()

    private val _connected = MutableStateFlow(false)
    override val connected = _connected.asStateFlow()

    private val outgoingSharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 200)
    private var supervisorScop = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var sessionJob: Job? = null
    private var generatorJob: Job? = null

    init {
        // initialize baseline prices
        val baseline = symbols.associateWith { sym ->
            val price = 50.0 + Random.nextDouble() * 1500.0
            PriceInfoDataModel(sym, price, price, System.currentTimeMillis(), true)
        }
        _prices.value = baseline
    }

    private fun ensureScope() {
        if (!supervisorScop.isActive) {
            supervisorScop = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
    }

    override fun start() {
        ensureScope()
        if (sessionJob != null) return

        sessionJob = supervisorScop.launch {
            while (isActive) {
                try {
                    client.webSocket(
                        method = HttpMethod.Get,
                        host = BASE_WEB_SOCKET_URL,
                        path = WEB_SOCKET_URL_RAW,
                        request = {
                            url {
                                protocol = URLProtocol.WSS
                            }
                        }
                    ) {
                        _connected.value = true
                        val sender = launch { sendMessageToWebSocket(this@webSocket) }
                        val receiver = launch { handleMessageFromSocket(this@webSocket) }

                        joinAll(sender, receiver)
                    }
                } catch (_: Throwable) {
                    _connected.value = false
                    delay(ONE_SEC)
                } finally {
                    _connected.value = false
                }
            }
        }

        // Generator that emits one update per symbol every 2 seconds
        sendPeriodicMessage()
    }

    private fun sendPeriodicMessage() {
        generatorJob = supervisorScop.launch {
            while (isActive) {
                symbols.forEach { sym ->
                    val last = _prices.value[sym]?.price ?: (HUNDRED + Random.nextDouble() * TWO_HUNDRED)
                    val change = (Random.nextDouble() - POINT_5) * (last * POINT_01)
                    val newPrice = (last + change).coerceAtLeast(POINT_01)
                    val payload = "${sym}|${"%.2f".format(newPrice)}"
                    outgoingSharedFlow.emit(payload)
                }
                delay(TWO_SEC)
            }
        }
    }

    private suspend fun sendMessageToWebSocket(webSocketSession: DefaultClientWebSocketSession) {
        outgoingSharedFlow.collect { msg ->
            try {
                webSocketSession.send(Frame.Text(msg))
            } catch (_: Throwable) {}
        }
    }

    private suspend fun handleMessageFromSocket(webSocketSession: DefaultClientWebSocketSession) {
        try {
            for (frame in webSocketSession.incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    // Parse "SYMBOL|PRICE"
                    val parts = text.split("|")
                    if (parts.size >= 2) {
                        val sym = parts[0]
                        val price = parts[1].toDoubleOrNull()
                        if (price != null) {
                            _prices.update { current ->
                                val prev = current[sym]?.price ?: price
                                val priceInfoMap = current.toMutableMap()
                                priceInfoMap[sym] = PriceInfoDataModel(
                                    sym,
                                    price,
                                    prev,
                                    System.currentTimeMillis()
                                )

                                priceInfoMap
                            }
                        }
                    }
                }
            }
        } catch (_: Throwable) {
        }
    }

    override fun stop() {
        generatorJob?.cancel()
        generatorJob = null

        sessionJob?.cancel()
        sessionJob = null

        _connected.value = false
        //client.close()
    }

    override fun close() {
        stop()
        supervisorScop.cancel()
    }
}

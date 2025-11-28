package com.tracker.stocktracker

import com.tracker.stocktracker.domain.AssetTrackerWebSocketRepository
import com.tracker.stocktracker.model.PriceInfoDataModel
import com.tracker.stocktracker.domain.AssetTrackerUseCase
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AssetTrackerUseCaseTest {

    private lateinit var mockRepository: AssetTrackerWebSocketRepository
    private lateinit var useCase: AssetTrackerUseCase

    private val testDispatcher = StandardTestDispatcher()

    private val pricesFlow =
        MutableStateFlow<Map<String, PriceInfoDataModel>>(emptyMap())
    private val connectedFlow = MutableStateFlow(false)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockRepository = mockk(relaxed = true)
        every { mockRepository.prices } returns pricesFlow
        every { mockRepository.connected } returns connectedFlow

        useCase = AssetTrackerUseCase(mockRepository)

        mockRepository = mockk(relaxed = true)
        useCase = AssetTrackerUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startSocketAndFetchData calls repo start`() {
        useCase.startSocketAndFetchData()
        verify { mockRepository.start() }
    }

    @Test
    fun `stopSocket calls repo stop`() {
        useCase.stopSocket()
        verify { mockRepository.stop() }
    }

    @Test
    fun `getStocksData maps repository data to UI data correctly`() = runBlocking {
        val repoData = mapOf(
            "GOOG" to PriceInfoDataModel("GOOG", 150.0, 140.0, System.currentTimeMillis(), initialData = false), // Price increased
            "AAPL" to PriceInfoDataModel("AAPL", 100.0, 110.0, System.currentTimeMillis(), initialData = false), // Price decreased
            "TSLA" to PriceInfoDataModel("TSLA", 200.0, 200.0, System.currentTimeMillis(), initialData = true) // Initial data point, no change
        )

        every { mockRepository.prices } returns MutableStateFlow(repoData)
        val uiDataList = useCase.getStocksData().first()

        assertEquals(3, uiDataList.size)

        val googUi = uiDataList.find { it.symbol == "GOOG" }
        val aaplUi = uiDataList.find { it.symbol == "AAPL" }
        val tslaUi = uiDataList.find { it.symbol == "TSLA" }

        assertEquals(150.0, googUi?.price ?: 0.0, 0.01)
        assertTrue(googUi?.showFlashGreen ?: false)
        assertEquals("+10.00", googUi?.differenceValue)

        assertEquals(100.0, aaplUi?.price ?: 0.0, 0.01)
        assertFalse(aaplUi?.showFlashGreen ?: true)
        assertEquals("-10.00", aaplUi?.differenceValue)

        assertEquals(200.0, tslaUi?.price ?: 0.0, 0.01)
        assertNull(tslaUi?.arrow)
    }

    @Test
    fun `getConnectionData delegates to repository connection status`() = runBlocking {
        val connectionFlow = MutableStateFlow(false)
        every { mockRepository.connected } returns connectionFlow

        val useCaseFlow = useCase.getConnectionData()

        assertFalse(useCaseFlow.first())

        connectionFlow.value = true
        assertTrue(useCaseFlow.first())
    }
}

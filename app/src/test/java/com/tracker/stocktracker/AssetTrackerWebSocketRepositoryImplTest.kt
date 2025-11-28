package com.tracker.stocktracker

import app.cash.turbine.test
import com.tracker.stocktracker.data.AssetTrackerWebSocketRepositoryImpl
import com.tracker.stocktracker.utils.ONE_SEC
import com.tracker.stocktracker.utils.TWO_SEC
import com.tracker.stocktracker.utils.symbols
import io.ktor.client.HttpClient
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AssetTrackerWebSocketRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockHttpClient: HttpClient
    private lateinit var repository: AssetTrackerWebSocketRepositoryImpl

    @Before
    fun setUp() {
        mockHttpClient = mockk(relaxed = true)
        repository = AssetTrackerWebSocketRepositoryImpl(mockHttpClient)
    }

    @After
    fun tearDown() {
        repository.close()
    }

    @Test
    fun `start initiates connection status flow`() = runTest(testDispatcher) {
        repository.connected.test {
            assertFalse(awaitItem())

            repository.start()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial prices are populated correctly in the flow`() = runTest(testDispatcher) {
        val initialPrices = repository.prices.first()
        assertEquals(symbols.size, initialPrices.size)
        assertTrue(initialPrices.containsKey("AAPL"))
        assertTrue(initialPrices.containsKey("GOOG"))
        assertTrue(initialPrices.values.all { it.initialData })
    }

    @Test
    fun `generator job periodically emits messages after start is called`() = runTest(testDispatcher) {

        repository.start()

        advanceTimeBy(TWO_SEC - ONE_SEC)

        advanceTimeBy(TWO_SEC + 50L)
    }
}
package com.tracker.stocktracker

import app.cash.turbine.test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tracker.stocktracker.domain.AssetTrackerUseCase
import com.tracker.stocktracker.model.PriceInfoUiDataModel
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.ui.viewmodel.AssetTrackerViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AssetTrackerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockUseCase: AssetTrackerUseCase
    private lateinit var viewModel: AssetTrackerViewModel

    private val mockStockUiDataModel = PriceInfoUiDataModel(
        symbol = "AAPL",
        price = 150.0,
        previousPrice = 140.0,
        timestamp = 1L,
        arrow = null,
        showFlashGreen = true,
        differenceValue = "+10.00"
    )
    private val mockStockList = listOf(mockStockUiDataModel)

    private val mockPricesFlow = MutableStateFlow(mockStockList)
    private val mockConnectionStatusFlow = MutableStateFlow(false)


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockUseCase = mockk(relaxed = true)

        every { mockUseCase.getStocksData() } returns mockPricesFlow as kotlinx.coroutines.flow.Flow<List<PriceInfoUiDataModel>>

        every { mockUseCase.getConnectionData() } returns mockConnectionStatusFlow
        every { mockUseCase.getCurrentConnectionStatus() } returns mockConnectionStatusFlow

        viewModel = AssetTrackerViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is set correctly to loading`() = runTest {
        val initialState = viewModel.viewState.first()
        assertTrue(initialState.isLoading)
        assertEquals(null, initialState.priceInfoUiDataModel)
        assertFalse(initialState.connectionStatus)
    }

    @Test
    fun `handleEvent InitConnectionAndStockData fetches data and updates UI state`() = runTest {
        viewModel.viewState.test {
            skipItems(1)

            viewModel.handleEvent(StockXEvent.InitConnectionAndStockData)

            val stateAfterData = awaitItem()

            assertFalse(stateAfterData.isLoading)
            assertEquals(mockStockList, stateAfterData.priceInfoUiDataModel)

            cancelAndIgnoreRemainingEvents()
        }

        verify { mockUseCase.getStocksData() }
    }


    @Test
    fun `ToggleStartStop when already connected calls stopSocket`() = runTest {
        mockConnectionStatusFlow.value = true

        viewModel.handleEvent(StockXEvent.ToggleStartStop)
        advanceUntilIdle()

        verify(exactly = 1) { mockUseCase.stopSocket() }
        verify(exactly = 0) { mockUseCase.startSocketAndFetchData() }
    }

    @Test
    fun `ToggleStartStop when disconnected calls startSocket and fetches data`() = runTest {
        mockConnectionStatusFlow.value = false

        viewModel.viewState.test {
            skipItems(1)

            viewModel.handleEvent(StockXEvent.ToggleStartStop)

            advanceUntilIdle()

            val finalState = awaitItem()
            assertFalse(finalState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { mockUseCase.startSocketAndFetchData() }
        verify(exactly = 0) { mockUseCase.stopSocket() }
    }
}

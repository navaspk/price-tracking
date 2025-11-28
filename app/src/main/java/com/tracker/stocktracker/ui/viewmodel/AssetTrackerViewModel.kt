package com.tracker.stocktracker.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.tracker.base.viewmodel.BaseViewModel
import com.tracker.stocktracker.domain.AssetTrackerUseCase
import com.tracker.stocktracker.model.events.StockXEffect
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.model.events.StockXUiState
import kotlinx.coroutines.launch

/**
 * ViewModel helps to get event from composable and perform necessary action and sent back the
 * state and effect to composable.
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
class AssetTrackerViewModel(private val useCase: AssetTrackerUseCase) :
    BaseViewModel<StockXEvent, StockXUiState, StockXEffect>() {

    override fun initialState(): StockXUiState = StockXUiState(isLoading = true)

    override fun handleEvent(event: StockXEvent) {
        when (event) {
            StockXEvent.InitConnectionAndStockData -> getStocksData(true)
            StockXEvent.ToggleStartStop -> performToggleStartStop()
        }
    }

    private fun getStocksData(loading: Boolean) {
        viewModelScope.launch {
            sendUIState {
                copy(isLoading = loading)
            }
            useCase.getStocksData().collect { data ->
                sendUIState {
                    copy(
                        isLoading = false,
                        priceInfoUiDataModel = data
                    )
                }
            }
        }
    }

    private fun performToggleStartStop() {
        val currentConnectionStatus = useCase.getCurrentConnectionStatus().value//viewState.value.connectionStatus

        getConnectionData()
        if (currentConnectionStatus) {
            useCase.stopSocket()
            return
        }

        getStocksData(false)
        useCase.startSocketAndFetchData()
    }

    private fun getConnectionData() {
        viewModelScope.launch {
            useCase.getConnectionData().collect { status ->
                sendUIState {
                    copy(
                        isLoading = false,
                        connectionStatus = status
                    )
                }
            }
        }
    }

    override fun onCleared() {
        useCase.stopSocket()
        super.onCleared()
    }
}

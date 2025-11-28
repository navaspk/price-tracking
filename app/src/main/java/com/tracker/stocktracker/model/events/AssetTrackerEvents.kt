package com.tracker.stocktracker.model.events

import com.tracker.base.ViewEffect
import com.tracker.base.ViewEvent
import com.tracker.base.ViewState
import com.tracker.stocktracker.model.PriceInfoUiDataModel

/**
 * Event class to set up event, state and effects
 * Effects is like SideEffect which helps to make some operation which is out of composable
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
sealed class StockXEvent : ViewEvent {
    object InitConnectionAndStockData : StockXEvent()
    object ToggleStartStop : StockXEvent()
}

data class StockXUiState(
    val isLoading: Boolean = false,
    val connectionStatus: Boolean = false,
    val priceInfoUiDataModel: List<PriceInfoUiDataModel>? = null
) : ViewState

class StockXEffect : ViewEffect

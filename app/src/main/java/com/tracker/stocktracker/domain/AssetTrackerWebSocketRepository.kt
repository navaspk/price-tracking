package com.tracker.stocktracker.domain

import com.tracker.stocktracker.model.PriceInfoDataModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Base repo helps to communicate to data layer to start / stop websocket
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
interface AssetTrackerWebSocketRepository {
    val prices: StateFlow<Map<String, PriceInfoDataModel>>
    val connected: StateFlow<Boolean>

    fun start()
    fun stop()
    fun close()
}
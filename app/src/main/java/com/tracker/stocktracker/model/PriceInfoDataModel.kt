package com.tracker.stocktracker.model

/**
 * Model class for storing stock info
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
data class PriceInfoDataModel(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long,
    val initialData: Boolean = false
)
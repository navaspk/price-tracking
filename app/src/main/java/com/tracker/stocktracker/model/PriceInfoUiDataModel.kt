package com.tracker.stocktracker.model

import com.tracker.stocktracker.utils.ZERO
import com.tracker.stocktracker.utils.getNameAndIconForCurrency

data class PriceInfoUiDataModel(
    val symbol: String,
    val price: Double = 0.0,
    val previousPrice: Double = 0.0,
    val timestamp: Long,
    val showFlashGreen: Boolean = false,
    val arrow: Int? = null,
    val differenceValue: String? = null
) {
    fun getNameAndIconOfCurrency() = getNameAndIconForCurrency(symbol)

    fun getCurrencyAndPrice() = String.format("%1s %.2f", "$", price)

    fun showDifferenceValue() = differenceValue != ZERO

}

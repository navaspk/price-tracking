package com.tracker.stocktracker.domain

import com.tracker.stocktracker.R
import com.tracker.stocktracker.model.PriceInfoUiDataModel
import com.tracker.stocktracker.utils.MINUS
import com.tracker.stocktracker.utils.PLUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Domain layer help to communicate with data layer and send back the data to ViewModel
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
class AssetTrackerUseCase(private val stockRepo: AssetTrackerWebSocketRepository) {

    fun getConnectionData(): Flow<Boolean> = stockRepo.connected

    fun getStocksData(): Flow<List<PriceInfoUiDataModel>> {
        return stockRepo.prices.map { priceInfoMap ->
            priceInfoMap.values
                .sortedByDescending { it.price }
                .map { dataItem ->
                    val diff = dataItem.price - dataItem.previousPrice
                    val up = diff >= 0.0

                    PriceInfoUiDataModel(
                        symbol = dataItem.symbol,
                        price = dataItem.price,
                        previousPrice = dataItem.previousPrice,
                        timestamp = dataItem.timestamp,
                        arrow = if (dataItem.initialData) null else if (dataItem.price > dataItem.previousPrice) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down,
                        showFlashGreen = dataItem.price > dataItem.previousPrice,
                        differenceValue = (if (up) PLUS else MINUS) + String.format(
                            "%.2f",
                            kotlin.math.abs(diff)
                        )
                    )
                }
        }
    }

    fun startSocketAndFetchData() = stockRepo.start()

    fun stopSocket() = stockRepo.stop()

    fun getCurrentConnectionStatus() = stockRepo.connected
}

package com.tracker.stocktracker.ui.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tracker.stocktracker.model.PriceInfoUiDataModel
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.ui.theme.Body14Medium
import com.tracker.stocktracker.ui.theme.Body16Medium
import com.tracker.stocktracker.ui.theme.font12Body
import com.tracker.stocktracker.ui.theme.padding16
import com.tracker.stocktracker.ui.theme.padding4
import com.tracker.stocktracker.ui.theme.size30
import com.tracker.stocktracker.ui.viewmodel.AssetTrackerViewModel
import org.koin.androidx.compose.koinViewModel


/**
 * Home screen build using LazyColumn and helps to show Stock symbols, price, status price and date
 * of modification
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
@Composable
fun StockTrackerHomeScreen(
    viewModel: AssetTrackerViewModel = koinViewModel()
) {

    val stockState by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.onEvent(StockXEvent.InitConnectionAndStockData)

    LazyColumn {
        stockState.priceInfoUiDataModel?.let { uiData ->
            items(uiData, key = { it.symbol }) { item ->
                StockRow(item)
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StockRow(dataModel: PriceInfoUiDataModel) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(font12Body.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            modifier = Modifier
                .size(size30.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape),
            imageVector = ImageVector.vectorResource(id = dataModel.getNameAndIconOfCurrency().second),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(padding16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dataModel.getNameAndIconOfCurrency().first,
                style = Body14Medium
            )
            Spacer(modifier = Modifier.height(padding4.dp))
            Text(
                dataModel.symbol,
                style = Body16Medium
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                dataModel.getCurrencyAndPrice(),
                style = Body16Medium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                dataModel.arrow?.let {
                    Image(
                        painter = painterResource(dataModel.arrow),
                        contentDescription = null
                    )
                }
                if (dataModel.showDifferenceValue())
                    Text(dataModel.differenceValue.orEmpty())
            }
        }
    }
}

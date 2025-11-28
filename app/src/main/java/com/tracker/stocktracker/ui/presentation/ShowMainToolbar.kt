package com.tracker.stocktracker.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tracker.stocktracker.R
import com.tracker.stocktracker.model.events.StockXEvent
import com.tracker.stocktracker.ui.theme.font16Body
import com.tracker.stocktracker.ui.theme.font20Body
import com.tracker.stocktracker.ui.viewmodel.AssetTrackerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Toolbar text, status of connection and toggle for websocket
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowMainToolBar(
    title: String,
    viewModel: AssetTrackerViewModel = koinViewModel()
) {
    val toolbarState by viewModel.viewState.collectAsStateWithLifecycle()

    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                fontSize = font20Body.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            ShowConnectionStatus(toolbarState.connectionStatus)
        },
        actions = {
            ShowActionButton(
                toggleStatus = toolbarState.connectionStatus,
                onClick = { viewModel.onEvent(StockXEvent.ToggleStartStop) }
            )
        }
    )
}

@Composable
private fun ShowConnectionStatus(connectionStatus: Boolean?) {
    Image(
        painter = if (connectionStatus == true)
            painterResource(R.drawable.ic_green_light)
        else painterResource(R.drawable.ic_red_light),
        contentDescription = null,
        modifier = Modifier.padding(start = font16Body.dp)
    )

}

@Composable
private fun ShowActionButton(toggleStatus: Boolean, onClick: () -> Unit) {
    Text(
        text = if (toggleStatus) stringResource(R.string.stock_x_stop) else stringResource(R.string.stock_x_start),
        modifier = Modifier
            .clickable { onClick() }
            .padding(end = font16Body.dp)
    )
}

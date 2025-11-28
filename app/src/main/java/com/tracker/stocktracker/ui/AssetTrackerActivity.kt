package com.tracker.stocktracker.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tracker.base.utils.EMPTY_STRING
import com.tracker.stocktracker.navigation.StockTrackerNavHost
import com.tracker.stocktracker.ui.presentation.ShowMainToolBar
import com.tracker.stocktracker.ui.theme.StockTrackerTheme

class AssetTrackerActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            var toolbarTitle by remember { mutableStateOf(EMPTY_STRING) }

            StockTrackerTheme {
                Scaffold(
                    topBar = {
                        ShowMainToolBar(
                            title = toolbarTitle
                        )
                    }
                ) { paddingValues ->
                    StockTrackerNavHost(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                    ) { toolBarText ->
                        toolbarTitle = toolBarText
                    }
                }
            }
        }
    }
}

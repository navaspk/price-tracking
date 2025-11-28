@file:Suppress("DEPRECATION")

package com.tracker.stocktracker.di

import com.tracker.stocktracker.domain.AssetTrackerWebSocketRepository
import com.tracker.stocktracker.domain.AssetTrackerUseCase
import com.tracker.stocktracker.ui.viewmodel.AssetTrackerViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val domainModule = module {
    single { AssetTrackerUseCase(get() as AssetTrackerWebSocketRepository) }
}

val viewModelModule = module {
    viewModel { AssetTrackerViewModel(get()) }
}
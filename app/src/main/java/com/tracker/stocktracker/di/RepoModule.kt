package com.tracker.stocktracker.di

import com.tracker.stocktracker.domain.AssetTrackerWebSocketRepository
import com.tracker.stocktracker.data.AssetTrackerWebSocketRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AssetTrackerWebSocketRepository> { AssetTrackerWebSocketRepositoryImpl(get()) }
}

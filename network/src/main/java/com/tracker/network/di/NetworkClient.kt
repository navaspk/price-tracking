package com.tracker.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.dsl.module

/**
 * Di for adding Ktor client for WebSocket
 *
 * Created by : Navas
 * Date : 27/11/2025
 */
val networkModule = module {

    // Provide HttpClient
    single {
        HttpClient(CIO) {
            // to make websocket support
            install(WebSockets)

            // Logging
            install(Logging) {
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }

            expectSuccess = true
        }
    }
}

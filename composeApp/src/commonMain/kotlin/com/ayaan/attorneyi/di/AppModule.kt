package com.ayaan.attorneyi.di

import com.ayaan.attorneyi.data.remote.NewsApiService
import com.ayaan.attorneyi.data.repository.NewsRepository
import com.ayaan.attorneyi.presentation.NewsViewModel
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
            }
        }
    }
}

val dataModule = module {
    single { NewsApiService(get()) }
    single { NewsRepository(get()) }
}

val viewModelModule = module {
    viewModel { NewsViewModel(get()) }
}

package com.ayaan.attorneyi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.ayaan.attorneyi.di.dataModule
import com.ayaan.attorneyi.di.networkModule
import com.ayaan.attorneyi.di.viewModelModule
import com.ayaan.attorneyi.presentation.legalUpdates.NewsScreen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(networkModule, dataModule, viewModelModule)
        }
    ) {
        MaterialTheme {
            NewsScreen()
        }
    }
}

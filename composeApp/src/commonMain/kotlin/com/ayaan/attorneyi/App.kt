package com.ayaan.attorneyi

import androidx.compose.runtime.*
import com.ayaan.attorneyi.di.appModules
import com.ayaan.attorneyi.presentation.legalUpdates.NewsScreen
import com.ayaan.attorneyi.presentation.ui.AttorneyITheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(appModules)
        }
    ) {
        AttorneyITheme {
            NewsScreen()
        }
    }
}

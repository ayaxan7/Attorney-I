package com.ayaan.attorneyi

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ayaan.attorneyi.di.appModules
import com.ayaan.attorneyi.presentation.navigation.AppNavigation
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
            AppNavigation()
        }
    }
}

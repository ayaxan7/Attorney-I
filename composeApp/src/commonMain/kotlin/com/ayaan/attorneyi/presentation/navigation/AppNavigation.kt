package com.ayaan.attorneyi.presentation.navigation

import androidx.compose.runtime.*
import com.ayaan.attorneyi.auth.AuthViewModel
import com.ayaan.attorneyi.presentation.auth.AuthScreen
import com.ayaan.attorneyi.presentation.legalUpdates.NewsScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = koinViewModel()
    val uiState by authViewModel.uiState.collectAsState()

    if (uiState.isSignedIn) {
        // User is authenticated, show main app content
        NewsScreen()
    } else {
        // User is not authenticated, show login screen
        AuthScreen(
            onAuthSuccess = {
                // Navigation will happen automatically via state change
            }
        )
    }
}

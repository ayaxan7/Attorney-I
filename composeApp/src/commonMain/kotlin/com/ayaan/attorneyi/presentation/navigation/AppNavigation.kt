package com.ayaan.attorneyi.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayaan.attorneyi.auth.AuthViewModel
import com.ayaan.attorneyi.presentation.auth.AuthScreen
import com.ayaan.attorneyi.presentation.legalUpdates.NewsScreen
import com.ayaan.attorneyi.utils.AppLogger
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = koinViewModel()
    val uiState by authViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    // Determine start destination based on authentication state
    val startDestination = if (uiState.isSignedIn && uiState.currentUser != null) {
        NavigationRoutes.News.route
    } else {
        NavigationRoutes.Auth.route
    }
    AppLogger.d("AppNavigation", "Start destination: $startDestination with isSignedIn=${uiState.isSignedIn} and currentUser=${uiState.currentUser}")
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        composable(NavigationRoutes.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(NavigationRoutes.News.route) {
                        popUpTo(NavigationRoutes.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavigationRoutes.News.route) {
            NewsScreen()
        }
    }
}


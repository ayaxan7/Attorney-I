package com.ayaan.attorneyi.presentation.navigation

sealed class NavigationRoutes(val route: String) {
    object Auth : NavigationRoutes("auth")
    object News : NavigationRoutes("news")
}

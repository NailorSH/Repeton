package com.nailorsh.repeton.features.navigation.routes

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "login")
}
package com.nailorsh.repeton.features.navigation.routes

import com.nailorsh.repeton.core.navigation.NavigationRoute

sealed class AuthScreen(override val route: String) : NavigationRoute {
    object Login : AuthScreen(route = "login")
    object RoleSelection : AuthScreen(route = "role")
    object SignUp : AuthScreen(route = "signup")
}
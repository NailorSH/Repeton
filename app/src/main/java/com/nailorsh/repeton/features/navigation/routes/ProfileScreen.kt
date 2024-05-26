package com.nailorsh.repeton.features.navigation.routes

import com.nailorsh.repeton.core.navigation.NavigationRoute

enum class ProfileScreen(override val route: String) : NavigationRoute {
    ABOUT("change_about"), SUBJECTS("tutor_subjects")
}
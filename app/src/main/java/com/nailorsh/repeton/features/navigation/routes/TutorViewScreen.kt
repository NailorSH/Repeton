package com.nailorsh.repeton.features.navigation.routes

sealed class TutorViewScreen(val route: String) {
    object TutorView : TutorViewScreen(route = "tutor_view/{id}")
}
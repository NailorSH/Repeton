package com.nailorsh.repeton.features.navigation.routes

sealed class LessonViewScreen(val route: String) {
    object Lesson : LessonViewScreen(route = "lesson/{id}")
}
package com.nailorsh.repeton.features.navigation.routes

import com.nailorsh.repeton.core.navigation.NavigationRoute

sealed class LessonCreationScreen(override val route: String) : NavigationRoute {
    object NewLesson : LessonCreationScreen(route = "new_lesson")
    object NewLessonSecond : LessonCreationScreen(route = "new_lesson_2")
}
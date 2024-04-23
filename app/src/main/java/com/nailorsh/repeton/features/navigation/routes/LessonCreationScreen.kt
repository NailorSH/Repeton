package com.nailorsh.repeton.features.navigation.routes

sealed class LessonCreationScreen(val route: String) {
    object NewLesson : LessonCreationScreen(route = "new_lesson")
    object NewLessonSecond : LessonCreationScreen(route = "new_lesson_2")
}
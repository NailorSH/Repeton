package com.nailorsh.repeton.features.navigation.routes

sealed interface LessonViewScreen {
    val route: String

    object Lesson : LessonViewScreen {
        private const val BASE_ROUTE = "lesson"
        const val ID_PARAM = "id"

        override val route: String = "$BASE_ROUTE/{$ID_PARAM}"
        fun createLessonRoute(lessonId: Int) = "$BASE_ROUTE/$lessonId"
    }
}
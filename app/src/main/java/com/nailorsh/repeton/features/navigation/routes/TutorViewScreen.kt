package com.nailorsh.repeton.features.navigation.routes

import com.nailorsh.repeton.core.navigation.NavigationRoute

sealed interface TutorViewScreen : NavigationRoute {
    override val route: String

    object TutorView : TutorViewScreen {
        private const val BASE_ROUTE = "tutor_view"
        const val ID_PARAM = "id"

        override val route: String = "$BASE_ROUTE/{$ID_PARAM}"
        fun createTutorViewRoute(lessonId: Int) = "$BASE_ROUTE/$lessonId"
    }
}
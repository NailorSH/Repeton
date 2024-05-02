package com.nailorsh.repeton.features.navigation.routes

import com.nailorsh.repeton.common.data.models.UserId

sealed interface TutorViewScreen : NavigationRoute {
    override val route: String

    object TutorView : TutorViewScreen {
        private const val BASE_ROUTE = "tutor_view"
        const val ID_PARAM = "id"

        override val route: String = "$BASE_ROUTE/{$ID_PARAM}"
        fun createTutorViewRoute(tutorId: UserId) = "$BASE_ROUTE/${tutorId.value}"
    }
}
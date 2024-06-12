package com.nailorsh.repeton.features.navigation.routes

import com.nailorsh.repeton.core.navigation.NavigationRoute

enum class Graph(override val route: String) : NavigationRoute {
    ROOT("root_graph"),
    AUTHENTICATION("auth_graph"),
    HOME("home_graph"),
    LESSON_VIEW("lesson_view_graph"),
    LESSON_CREATION("lesson_creation_graph"),
    ABOUT("change_about"),
    TUTOR_VIEW("search_graph")
}
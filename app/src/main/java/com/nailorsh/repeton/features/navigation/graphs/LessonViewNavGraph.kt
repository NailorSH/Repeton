package com.nailorsh.repeton.features.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.currentlesson.presentation.ui.LessonScreen
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.LessonViewScreen

fun NavGraphBuilder.lessonViewNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.LESSON_VIEW,
        startDestination = LessonViewScreen.Lesson.route
    ) {
        composable(route = LessonViewScreen.Lesson.route) { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                val viewModel =
                    backStackEntry.sharedViewModel<CurrentLessonViewModel>(navController)

                LessonScreen(
                    lessonId = id.toInt(),
                    viewModel = viewModel
                )
            }
        }
    }
}
package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.LessonCreationScreen
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreen
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreenSecond
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel

fun NavGraphBuilder.lessonCreationNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.LESSON_CREATION.route,
        startDestination = LessonCreationScreen.NewLesson.route
    ) {
        composable(route = LessonCreationScreen.NewLesson.route) {
            val viewModel = it.sharedViewModel<NewLessonViewModel>(navController)
            val lessonState by viewModel.state.collectAsState()
            val filteredSubjects by viewModel.filteredSubjects.collectAsState()

            NewLessonScreen(
                lessonState = lessonState,
                filteredSubjects = filteredSubjects,
                onFilterSubjects = viewModel::updateFilteredSubjects,
                onNavigateBack = {
                    navController.navigateUp()
                    viewModel.clearData()
                },
                onNavigateNext = {
                    navController.navigate(LessonCreationScreen.NewLessonSecond.route)
                    viewModel.getSubjects()
                },
                onSaveRequiredFields = viewModel::saveRequiredFields
            )
        }

        composable(route = LessonCreationScreen.NewLessonSecond.route) {
            val viewModel = it.sharedViewModel<NewLessonViewModel>(navController)
            val lessonState by viewModel.state.collectAsState()

            NewLessonScreenSecond(
                lessonState = lessonState,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateSuccessfulSave = {
                    navController.popBackStack(
                        route = BottomBarScreen.Home.route,
                        inclusive = false
                    )
                    viewModel.clearData()
                },
                onSaveLesson = { description, homework, additionalMaterials ->
                    viewModel.saveLesson(description, homework, additionalMaterials)
                }
            )
        }
    }
}
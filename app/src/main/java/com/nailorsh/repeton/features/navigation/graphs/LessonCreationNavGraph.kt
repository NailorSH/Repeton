package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.LessonCreationScreen
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreen
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreenSecond
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel

fun NavGraphBuilder.lessonCreationNavGraph(
    navController: NavHostController,
    newLessonViewModel: NewLessonViewModel
) {
    navigation(
        route = Graph.LESSON_CREATION,
        startDestination = LessonCreationScreen.NewLesson.route
    ) {
        composable(route = LessonCreationScreen.NewLesson.route) {
            val lessonState by newLessonViewModel.state.collectAsState()
            val filteredSubjects by newLessonViewModel.filteredSubjects.collectAsState()

            NewLessonScreen(
                lessonState = lessonState,
                filteredSubjects = filteredSubjects,
                onFilterSubjects = newLessonViewModel::updateFilteredSubjects,
                onNavigateBack = {
                    navController.navigateUp()
                    newLessonViewModel.clearData()
                },
                onNavigateNext = {
                    navController.navigate(LessonCreationScreen.NewLessonSecond.route)
                    newLessonViewModel.getSubjects()
                },
                onSaveRequiredFields = newLessonViewModel::saveRequiredFields
            )
        }

        composable(route = LessonCreationScreen.NewLessonSecond.route) {
            val lessonState by newLessonViewModel.state.collectAsState()
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
                    newLessonViewModel.clearData()
                },
                onSaveLesson = { description, homework, additionalMaterials ->
                    newLessonViewModel.saveLesson(description, homework, additionalMaterials)
                }
            )
        }
    }
}
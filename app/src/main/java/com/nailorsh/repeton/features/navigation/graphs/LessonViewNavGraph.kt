package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.currentlesson.presentation.ui.LessonScreen
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.homework.presentation.ui.HomeworkScreen
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkNavigationEvent
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkViewModel
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.LessonViewScreen

fun NavGraphBuilder.lessonViewNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.LESSON_VIEW.route,
        startDestination = LessonViewScreen.Lesson.route
    ) {
        composable(route = LessonViewScreen.Lesson.route) { backStackEntry ->
            backStackEntry.arguments?.getString(LessonViewScreen.Lesson.ID_PARAM)?.let { id ->
                val viewModel =
                    backStackEntry.sharedViewModel<CurrentLessonViewModel>(navController)

                LessonScreen(
                    lessonId = Id(id),
                    viewModel = viewModel,
                    onNavigateHomework = {
                        navController.navigate(
                            LessonViewScreen.Homework.createHomeworkRoute(
                                Id(id)
                            )
                        )
                    }
                )
            }
        }
        composable(route = LessonViewScreen.Homework.route) { backStackEntry ->
            backStackEntry.arguments?.getString(LessonViewScreen.Homework.ID_PARAM)?.let { id ->
                val viewModel = hiltViewModel<HomeworkViewModel>()
                LaunchedEffect(Unit) { viewModel.getHomework(Id(id)) }
                val navigationEvents = viewModel.navigationEvent
                val state = viewModel.state.collectAsState()

                val lifecycleOwner = LocalLifecycleOwner.current

                LaunchedEffect(lifecycleOwner.lifecycle) {
                    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        navigationEvents.collect { navigationEvent ->
                            when (navigationEvent) {
                                HomeworkNavigationEvent.NavigateBack -> navController.popBackStack()
                            }
                        }
                    }
                }
                HomeworkScreen(
                    uiState = state.value,
                    onAction = viewModel::onAction,
                )
            }
        }
    }
}
package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.LessonCreationScreen
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreen
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonNavigationEvent
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
            val uiState by viewModel.state.collectAsState()
            val eventsChannel = viewModel.uiEventsChannel
            val navigationChannel = viewModel.navigationEventsChannel

            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(lifecycleOwner.lifecycle) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    navigationChannel.collect { navigationEvent ->
                        when (navigationEvent) {
                            NewLessonNavigationEvent.NavigateBack -> navController.popBackStack()
                            is NewLessonNavigationEvent.NavigateToNext -> {
                                /* TODO("Navigation to the second screen") */
                            }
                        }
                    }
                }
            }

            NewLessonScreen(
                uiState = uiState,
                eventChannel = eventsChannel,
                onCallback = viewModel::onCallback
            )
        }

        composable(route = LessonCreationScreen.NewLessonSecond.route) {
            val viewModel = it.sharedViewModel<NewLessonViewModel>(navController)
            val lessonState by viewModel.state.collectAsState()

//            NewLessonScreenSecond(
//                lessonState = lessonState,
//                onNavigateBack = {
//                    navController.navigateUp()
//                },
//                onNavigateSuccessfulSave = {
//                    navController.popBackStack(
//                        route = BottomBarScreen.Home.route,
//                        inclusive = false
//                    )
//                    viewModel.clearData()
//                },
//                onSaveLesson = { description, homework, additionalMaterials ->
//                    viewModel.saveLesson(description, homework, additionalMaterials)
//                }
//            )
        }
    }
}
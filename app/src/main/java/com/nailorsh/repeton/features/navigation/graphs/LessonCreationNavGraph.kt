package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstNavigationEvent
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstViewModel
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondNavigationEvent
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondViewModel
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSharedViewModel

fun NavGraphBuilder.lessonCreationNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.LESSON_CREATION.route,
        startDestination = LessonCreationScreen.NewLesson.route
    ) {
        composable(route = LessonCreationScreen.NewLesson.route) {
            val viewModel = hiltViewModel<NewLessonFirstViewModel>()
            val sharedViewModel = it.sharedViewModel<NewLessonSharedViewModel>(navController)
            val uiState by viewModel.state.collectAsState()
            val uiEventChannel = viewModel.uiEvents
            val navigationChannel = viewModel.navigationEvents

            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(lifecycleOwner.lifecycle) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    navigationChannel.collect { navigationEvent ->
                        when (navigationEvent) {
                            NewLessonFirstNavigationEvent.NavigateBack -> navController.popBackStack(
                                route = BottomBarScreen.Home.route,
                                inclusive = false
                            )

                            is NewLessonFirstNavigationEvent.NavigateToNext -> {
                                sharedViewModel.updateState(navigationEvent.firstScreenData)
                                navController.navigate(LessonCreationScreen.NewLessonSecond.route)
                            }


                        }
                    }
                }
            }

            NewLessonScreen(
                uiState = uiState,
                uiEventChannel = uiEventChannel,
                onAction = viewModel::onAction
            )
        }
        composable(route = LessonCreationScreen.NewLessonSecond.route) {
            val viewModel = hiltViewModel<NewLessonSecondViewModel>()
            val sharedViewModel = it.sharedViewModel<NewLessonSharedViewModel>(navController)
            val firstScreenData by sharedViewModel.state.collectAsState()

            val lessonState by viewModel.state.collectAsState()
            val navigationChannel = viewModel.navigationEventsChannel
            val uiEventChannel = viewModel.uiEventsChannel
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(Unit) {
                viewModel.passFirstScreenData(firstScreenData)
            }

            LaunchedEffect(lifecycleOwner.lifecycle) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    navigationChannel.collect { navigationEvent ->
                        when (navigationEvent) {
                            NewLessonSecondNavigationEvent.NavigateBack -> navController.navigateUp()
                            NewLessonSecondNavigationEvent.SaveLesson -> navController.popBackStack(
                                route = BottomBarScreen.Home.route,
                                inclusive = false
                            )
                        }
                    }
                }
            }

            NewLessonScreenSecond(
                uiState = lessonState,
                onAction = viewModel::onAction,
                uiEventChannel = uiEventChannel
            )
        }
    }
}
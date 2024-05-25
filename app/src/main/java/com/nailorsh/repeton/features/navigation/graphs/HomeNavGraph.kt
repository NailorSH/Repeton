package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.features.about.presentation.ui.AboutScreen
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutNavigationEvent
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutViewModel
import com.nailorsh.repeton.features.messenger.presentation.ui.ChatsScreen
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.LessonViewScreen
import com.nailorsh.repeton.features.navigation.routes.ProfileScreen
import com.nailorsh.repeton.features.navigation.routes.TutorViewScreen
import com.nailorsh.repeton.features.schedule.presentation.ui.ScheduleScreen
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleNavigationEvent
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.tutorsearch.presentation.ui.SearchScreen
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel
import com.nailorsh.repeton.features.userprofile.presentation.ui.ProfileScreen
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = Graph.HOME.route,
        startDestination = BottomBarScreen.Home.route,
        modifier = modifier
    ) {
        tutorViewNavGraph(navController)

        composable(route = BottomBarScreen.Search.route) {
            val tutorSearchViewModel = hiltViewModel<TutorSearchViewModel>()

            SearchScreen(
                getSearchResults = tutorSearchViewModel::getTutors,
                typingGetSearchResults = tutorSearchViewModel::typingTutorSearch,
                searchUiState = tutorSearchViewModel.searchUiState,
                onTutorCardClicked = { tutorId ->
                    navController.navigate(TutorViewScreen.TutorView.createTutorViewRoute(tutorId))
                }
            )
        }

        composable(route = BottomBarScreen.Home.route) {
            val scheduleViewModel = hiltViewModel<ScheduleViewModel>()

            val lifecycleOwner = LocalLifecycleOwner.current
            val navigationEvents = scheduleViewModel.navigationEvents
            val uiEvents = scheduleViewModel.uiEvents
            LaunchedEffect(lifecycleOwner.lifecycle) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    navigationEvents.collect { navigationEvent ->
                        when (navigationEvent) {
                            is ScheduleNavigationEvent.NavigateToLesson -> navController.navigate(
                                LessonViewScreen.Lesson.createLessonRoute(navigationEvent.lesson.id)
                            )

                            ScheduleNavigationEvent.NavigateToNewLesson -> navController.navigate(
                                Graph.LESSON_CREATION.route
                            )
                        }
                    }
                }
            }

            ScheduleScreen(
                scheduleUiState = scheduleViewModel.state.collectAsState().value,
                uiEvents = uiEvents,
                onAction = scheduleViewModel::onAction
            )
        }

        lessonViewNavGraph(navController)

        lessonCreationNavGraph(navController)

        composable(route = BottomBarScreen.Chats.route) {
            val messengerViewModel = hiltViewModel<MessengerViewModel>()
//            HomeworkScreen()
            ChatsScreen(
                getChats = messengerViewModel::getChats,
                chatsUiState = messengerViewModel.chatsUiState
            )
        }

        composable(route = BottomBarScreen.Profile.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                profileScreenUiState = profileViewModel.uiState.collectAsState().value,
                sideEffectState = profileViewModel.sideEffect,
                onOptionNavigate = { navController.navigate(it.route) },
                onOptionClicked = profileViewModel::onOptionClicked
            )
        }

        composable(route = ProfileScreen.ABOUT.route) {
            val aboutViewModel = hiltViewModel<AboutViewModel>()

            val lifecycleOwner = LocalLifecycleOwner.current
            val navigationEvents = aboutViewModel.navigationEvents
            val uiEvents = aboutViewModel.uiEvents
            LaunchedEffect(lifecycleOwner.lifecycle) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    navigationEvents.collect { navigationEvent ->
                        when (navigationEvent) {
                            AboutNavigationEvent.NavigateBack -> navController.popBackStack()
                        }
                    }
                }
            }
            AboutScreen(
                state = aboutViewModel.state.collectAsState().value,
                onAction = aboutViewModel::onAction,
                uiEvents = uiEvents
            )
        }
    }
}
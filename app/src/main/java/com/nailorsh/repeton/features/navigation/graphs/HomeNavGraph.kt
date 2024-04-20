package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.features.messenger.presentation.ui.ChatsScreen
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.schedule.presentation.ui.ScheduleScreen
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.studentprofile.presentation.ui.ProfileScreen
import com.nailorsh.repeton.features.tutorsearch.presentation.ui.SearchScreen
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
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
                    navController.navigate("tutor_view/${tutorId}")
                }
            )
        }

        composable(route = BottomBarScreen.Home.route) {
            val scheduleViewModel = hiltViewModel<ScheduleViewModel>()

            ScheduleScreen(
                scheduleUiState = scheduleViewModel.scheduleUiState,
                // Вызов getLessons по указанной дате
                getLessons = { scheduleViewModel.getLessons() },
                onLessonClicked = { lesson ->
                    navController.navigate("lesson/${lesson.id}")
                },
                onNewLessonClicked = {
                    navController.navigate(Graph.LESSON_CREATION)
                }
            )
        }

        lessonViewNavGraph(navController)

        lessonCreationNavGraph(navController)

        composable(route = BottomBarScreen.Chats.route) {
            val messengerViewModel = hiltViewModel<MessengerViewModel>()

            ChatsScreen(
                getChats = messengerViewModel::getChats,
                chatsUiState = messengerViewModel.chatsUiState
            )
        }

        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}
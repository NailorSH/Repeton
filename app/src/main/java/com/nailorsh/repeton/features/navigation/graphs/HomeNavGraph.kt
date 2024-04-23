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
import com.nailorsh.repeton.features.navigation.routes.LessonViewScreen
import com.nailorsh.repeton.features.navigation.routes.TutorViewScreen
import com.nailorsh.repeton.features.schedule.presentation.ui.ScheduleScreen
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.userprofile.presentation.ui.ProfileScreen
import com.nailorsh.repeton.features.tutorsearch.presentation.ui.SearchScreen
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileViewModel

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
                    navController.navigate(TutorViewScreen.TutorView.createTutorViewRoute(tutorId))
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
                    navController.navigate(LessonViewScreen.Lesson.createLessonRoute(lesson.id))
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
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(

            )

        }
    }
}
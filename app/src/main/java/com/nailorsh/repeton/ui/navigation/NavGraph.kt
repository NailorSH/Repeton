package com.nailorsh.repeton.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.domain.viewmodels.CurrentLessonViewModel
import com.nailorsh.repeton.domain.viewmodels.MessengerViewModel
import com.nailorsh.repeton.domain.viewmodels.ScheduleViewModel
import com.nailorsh.repeton.domain.viewmodels.TutorSearchViewModel
import com.nailorsh.repeton.ui.screens.ChatsScreen
import com.nailorsh.repeton.ui.screens.LessonScreen
import com.nailorsh.repeton.ui.screens.ProfileScreen
import com.nailorsh.repeton.ui.screens.ScheduleScreen
import com.nailorsh.repeton.ui.screens.SearchScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    currentLessonViewModel: CurrentLessonViewModel,
    tutorSearchViewModel: TutorSearchViewModel,
    scheduleViewModel: ScheduleViewModel,
    messengerViewModel: MessengerViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = AppSections.HOME.route,
        modifier = modifier
    ) {
        composable(AppSections.SEARCH.route) {
            SearchScreen(
                getSearchResults = tutorSearchViewModel::getTutors,
                typingGetSearchResults = tutorSearchViewModel::typingTutorSearch,
//                getSearchResults = viewModel::getTutors,
                searchUiState = tutorSearchViewModel.searchUiState
            )
        }
        composable(AppSections.HOME.route) {

            ScheduleScreen(
                scheduleUiState = scheduleViewModel.scheduleUiState,
                // Вызов getLessons по указанной дате
                getLessons = { scheduleViewModel.getLessons() },
                onLessonClicked = { lesson ->
                    navHostController.navigate("lesson/${lesson.id}")
                }

            )
        }
        composable(AppSections.CHATS.route) {
            ChatsScreen(
                getChats = messengerViewModel::getChats,
                chatsUiState = messengerViewModel.chatsUiState
            )
        }
        composable(AppSections.PROFILE.route) {
            ProfileScreen()
        }

        composable(AppSections.LESSON.route) { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                LessonScreen(lessonId = id.toInt())
            }
        }
    }
}
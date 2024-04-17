package com.nailorsh.repeton.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.features.currentlesson.presentation.ui.LessonScreen
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.messenger.presentation.ui.ChatsScreen
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreen
import com.nailorsh.repeton.features.newlesson.presentation.ui.NewLessonScreenSecond
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import com.nailorsh.repeton.features.schedule.presentation.ui.ScheduleScreen
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.studentprofile.presentation.ui.ProfileScreen
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.TutorProfileScreen
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel
import com.nailorsh.repeton.features.tutorsearch.presentation.ui.SearchScreen
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    currentLessonViewModel: CurrentLessonViewModel,
    tutorSearchViewModel: TutorSearchViewModel,
    tutorProfileViewModel: TutorProfileViewModel,
    scheduleViewModel: ScheduleViewModel,
    messengerViewModel: MessengerViewModel,
    newLessonViewModel: NewLessonViewModel,
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
                searchUiState = tutorSearchViewModel.searchUiState,
                onTutorCardClicked = { tutorId ->
                    navHostController.navigate("tutor/${tutorId}")
                }
            )
        }
        composable(AppSections.HOME.route) {
            ScheduleScreen(
                scheduleUiState = scheduleViewModel.scheduleUiState,
                // Вызов getLessons по указанной дате
                getLessons = { scheduleViewModel.getLessons() },
                onLessonClicked = { lesson ->
                    navHostController.navigate("lesson/${lesson.id}")
                },
                onNewLessonClicked = {
                    navHostController.navigate(AppSections.NEW_LESSON.route)
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
                LessonScreen(
                    lessonId = id.toInt(),
                    viewModel = currentLessonViewModel
                )
            }
        }

        composable(AppSections.TUTOR.route) { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                TutorProfileScreen(
                    tutorId = id.toInt(),
                    onBackClicked = { navHostController.popBackStack() },
                    viewModel = tutorProfileViewModel
                )
            }
            
        composable(AppSections.NEW_LESSON.route) {
            val lessonState by newLessonViewModel.state.collectAsState()
            val filteredSubjects by newLessonViewModel.filteredSubjects.collectAsState()

            NewLessonScreen(
                lessonState = lessonState,
                filteredSubjects = filteredSubjects,
                onFilterSubjects = newLessonViewModel::updateFilteredSubjects,
                onNavigateBack = {
                    navHostController.navigateUp()
                    newLessonViewModel.clearData()
                },
                onNavigateNext = {
                    navHostController.navigate(AppSections.NEW_LESSON_SECOND.route)
                    newLessonViewModel.getSubjects()
                },
                onSaveRequiredFields = newLessonViewModel::saveRequiredFields
            )
        }

        composable(AppSections.NEW_LESSON_SECOND.route) {
            val lessonState by newLessonViewModel.state.collectAsState()
            NewLessonScreenSecond(
                lessonState = lessonState,
                onNavigateBack = {
                    navHostController.navigateUp()
                },
                onNavigateSuccessfulSave = {
                    navHostController.popBackStack(route = AppSections.HOME.route, inclusive = false)
                    newLessonViewModel.clearData()
                },
                onSaveLesson = { description, homework, additionalMaterials ->
                    newLessonViewModel.saveLesson(description, homework, additionalMaterials)
                }
            )
        }
    }
}
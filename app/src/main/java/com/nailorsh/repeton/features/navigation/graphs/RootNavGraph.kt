package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.nailorsh.repeton.features.auth.presentation.ui.PhoneLoginUI
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.auth.presentation.viewmodel.Response
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.ui.HomeScreen
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    tutorSearchViewModel: TutorSearchViewModel,
    tutorProfileViewModel: TutorProfileViewModel,
    scheduleViewModel: ScheduleViewModel,
    currentLessonViewModel: CurrentLessonViewModel,
    newLessonViewModel: NewLessonViewModel,
    messengerViewModel: MessengerViewModel
) {
    val authUiState by authViewModel.signUpState.collectAsState()
    if (authUiState is Response.Success) {
        HomeScreen(
            tutorSearchViewModel = tutorSearchViewModel,
            tutorProfileViewModel = tutorProfileViewModel,
            scheduleViewModel = scheduleViewModel,
            currentLessonViewModel = currentLessonViewModel,
            newLessonViewModel = newLessonViewModel,
            messengerViewModel = messengerViewModel
        )
    } else {
        PhoneLoginUI(
            viewModel = authViewModel,
            popUpScreen = {
                navController.popBackStack()
                navController.navigate(Graph.HOME)
            }
        )
    }

//    NavHost(
//        navController = navController,
//        route = Graph.ROOT,
//        startDestination = Graph.AUTHENTICATION,
//    ) {
//        authNavGraph(
//            navController = navController,
//            authViewModel = authViewModel
//        )
//
//        composable(route = Graph.HOME) {
//            HomeScreen(
//                tutorSearchViewModel = tutorSearchViewModel,
//                tutorProfileViewModel = tutorProfileViewModel,
//                scheduleViewModel = scheduleViewModel,
//                currentLessonViewModel = currentLessonViewModel,
//                newLessonViewModel = newLessonViewModel,
//                messengerViewModel = messengerViewModel
//            )
//        }
//    }
}
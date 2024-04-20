package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nailorsh.repeton.features.auth.presentation.ui.PhoneLoginUI
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.auth.presentation.viewmodel.Response
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.ui.HomeScreen

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authUiState by authViewModel.signUpState.collectAsState()

    if (authUiState is Response.Success) {
        HomeScreen()
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
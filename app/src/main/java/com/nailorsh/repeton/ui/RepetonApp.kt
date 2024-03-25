package com.nailorsh.repeton.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.domain.viewmodels.AuthViewModel
import com.nailorsh.repeton.domain.viewmodels.CurrentLessonViewModel
import com.nailorsh.repeton.domain.viewmodels.MessengerViewModel
import com.nailorsh.repeton.domain.viewmodels.Response
import com.nailorsh.repeton.domain.viewmodels.ScheduleViewModel
import com.nailorsh.repeton.domain.viewmodels.TutorSearchViewModel
import com.nailorsh.repeton.ui.navigation.AppSections
import com.nailorsh.repeton.ui.navigation.NavGraph
import com.nailorsh.repeton.ui.navigation.RepetonBottomBar
import com.nailorsh.repeton.ui.screens.auth.PhoneLoginUI
import com.nailorsh.repeton.ui.theme.RepetonTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RepetonApp(
    navController: NavHostController = rememberNavController(),
    currentLessonViewModel: CurrentLessonViewModel = viewModel(),
    tutorSearchViewModel: TutorSearchViewModel = viewModel(),
    scheduleViewModel: ScheduleViewModel = viewModel(),
    messengerViewModel: MessengerViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    RepetonTheme {
        val authUiState by authViewModel.signUpState.collectAsState()
        if (authUiState is Response.Success) {
            Scaffold(
                bottomBar = {
                    RepetonBottomBar(
                        tabs = listOf(
                            AppSections.SEARCH,
                            AppSections.HOME,
                            AppSections.CHATS,
                            AppSections.PROFILE
                        ).toTypedArray(),

                        navController = navController
                    )
                }
            ) { innerPaddingModifier ->
                NavGraph(
                    navHostController = navController,
                    currentLessonViewModel = currentLessonViewModel,
                    tutorSearchViewModel = tutorSearchViewModel,
                    scheduleViewModel = scheduleViewModel,
                    messengerViewModel = messengerViewModel,
                    modifier = Modifier.padding(innerPaddingModifier)
                )
            }
        } else {
            PhoneLoginUI(
                viewModel = authViewModel,
                popUpScreen = {}
            )
        }
    }
}
package com.nailorsh.repeton.navigation.ui

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
import com.nailorsh.repeton.auth.presentation.ui.PhoneLoginUI
import com.nailorsh.repeton.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.auth.presentation.viewmodel.Response
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.navigation.AppSections
import com.nailorsh.repeton.navigation.NavGraph
import com.nailorsh.repeton.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.tutorsearch.presentation.viewmodel.TutorSearchViewModel

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
        if (true) {
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
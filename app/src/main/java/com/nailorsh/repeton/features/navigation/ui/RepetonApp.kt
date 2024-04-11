package com.nailorsh.repeton.features.navigation.ui

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
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.auth.presentation.ui.PhoneLoginUI
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.auth.presentation.viewmodel.Response
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.navigation.AppSections
import com.nailorsh.repeton.features.navigation.NavGraph
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RepetonApp(
    navController: NavHostController = rememberNavController(),
    currentLessonViewModel: CurrentLessonViewModel = viewModel(),
    tutorSearchViewModel: TutorSearchViewModel = viewModel(),
    scheduleViewModel: ScheduleViewModel = viewModel(),
    messengerViewModel: MessengerViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    newLessonViewModel: NewLessonViewModel = viewModel(),
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
                    newLessonViewModel = newLessonViewModel,
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
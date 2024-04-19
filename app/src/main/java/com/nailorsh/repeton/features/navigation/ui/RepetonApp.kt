package com.nailorsh.repeton.features.navigation.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.navigation.graphs.RootNavGraph
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel

@Composable
fun RepetonApp(
    authViewModel: AuthViewModel = viewModel(),
    tutorProfileViewModel: TutorProfileViewModel = viewModel(),
    tutorSearchViewModel: TutorSearchViewModel = viewModel(),
    scheduleViewModel: ScheduleViewModel = viewModel(),
    currentLessonViewModel: CurrentLessonViewModel = viewModel(),
    newLessonViewModel: NewLessonViewModel = viewModel(),
    messengerViewModel: MessengerViewModel = viewModel()
) {
    RepetonTheme {
        RootNavGraph(
            navController = rememberNavController(),
            authViewModel = authViewModel,
            tutorSearchViewModel = tutorSearchViewModel,
            tutorProfileViewModel = tutorProfileViewModel,
            scheduleViewModel = scheduleViewModel,
            currentLessonViewModel = currentLessonViewModel,
            newLessonViewModel = newLessonViewModel,
            messengerViewModel = messengerViewModel
        )
    }
}
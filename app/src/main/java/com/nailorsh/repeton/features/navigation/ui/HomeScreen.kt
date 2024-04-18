package com.nailorsh.repeton.features.navigation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.MessengerViewModel
import com.nailorsh.repeton.features.navigation.graphs.HomeNavGraph
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchViewModel

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    tutorSearchViewModel: TutorSearchViewModel,
    tutorProfileViewModel: TutorProfileViewModel,
    scheduleViewModel: ScheduleViewModel,
    currentLessonViewModel: CurrentLessonViewModel,
    newLessonViewModel: NewLessonViewModel,
    messengerViewModel: MessengerViewModel
) {
    Scaffold(
        bottomBar = {
            RepetonBottomBar(
                tabs = listOf(
                    BottomBarScreen.Search,
                    BottomBarScreen.Home,
                    BottomBarScreen.Chats,
                    BottomBarScreen.Profile
                ).toTypedArray(),

                navController = navController
            )
        }
    ) { innerPaddingModifier ->
        HomeNavGraph(
            navController = navController,
            tutorSearchViewModel = tutorSearchViewModel,
            tutorProfileViewModel = tutorProfileViewModel,
            scheduleViewModel = scheduleViewModel,
            currentLessonViewModel = currentLessonViewModel,
            newLessonViewModel = newLessonViewModel,
            messengerViewModel = messengerViewModel,
            modifier = Modifier.padding(innerPaddingModifier)
        )
    }
}
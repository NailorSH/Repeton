package com.nailorsh.repeton.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.ui.screens.ChatsScreen
import com.nailorsh.repeton.ui.screens.ProfileScreen
import com.nailorsh.repeton.ui.screens.ScheduleScreen
import com.nailorsh.repeton.ui.screens.SearchScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = AppSections.HOME.route,
        modifier = modifier
    ) {
        composable(AppSections.SEARCH.route) {
            SearchScreen()
        }
        composable(AppSections.HOME.route) {
            ScheduleScreen()
        }
        composable(AppSections.CHATS.route) {
            ChatsScreen()
        }
        composable(AppSections.PROFILE.route) {
            ProfileScreen()
        }
    }
}
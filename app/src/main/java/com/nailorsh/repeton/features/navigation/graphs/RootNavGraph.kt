package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.navigation.presentation.ui.HomeScreen
import com.nailorsh.repeton.features.navigation.routes.Graph

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val isUserAuthenticated =
        authViewModel.isUserAuthenticated.collectAsState(initial = false).value

    NavHost(
        navController = navController,
        route = Graph.ROOT.route,
        startDestination = if (isUserAuthenticated) Graph.HOME.route else Graph.AUTHENTICATION.route,
    ) {
        authNavGraph(navController)

        composable(route = Graph.HOME.route) {
            HomeScreen()
        }
    }
}
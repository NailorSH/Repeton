package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.features.navigation.presentation.ui.HomeScreen
import com.nailorsh.repeton.features.navigation.routes.Graph

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT.route,
        startDestination = Graph.AUTHENTICATION.route,
    ) {
        authNavGraph(navController)

        composable(route = Graph.HOME.route) {
            HomeScreen()
        }
    }
}
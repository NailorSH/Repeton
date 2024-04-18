package com.nailorsh.repeton.features.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.features.auth.presentation.ui.PhoneLoginUI
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.navigation.routes.AuthScreen
import com.nailorsh.repeton.features.navigation.routes.Graph

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            PhoneLoginUI(
                viewModel = authViewModel,
                popUpScreen = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                }
            )
        }
    }
}
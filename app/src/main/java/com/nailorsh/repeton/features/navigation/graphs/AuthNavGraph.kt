package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.auth.presentation.ui.signin.PhoneLoginUI
import com.nailorsh.repeton.features.auth.presentation.ui.signup.NameInputScreen
import com.nailorsh.repeton.features.auth.presentation.ui.signup.RoleSelectionScreen
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.navigation.routes.AuthScreen
import com.nailorsh.repeton.features.navigation.routes.Graph

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.AUTHENTICATION.route,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

            PhoneLoginUI(
                viewModel = authViewModel,
                navigateNext = {
                    authViewModel.checkUserExists { exists ->
                        if (exists) {
                            navController.popBackStack()
                            navController.navigate(Graph.HOME.route)
                        } else {
                            navController.navigate(AuthScreen.RoleSelection.route)
                        }
                    }

                }
            )
        }

        composable(route = AuthScreen.RoleSelection.route) {
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val newUserState by authViewModel.newUserUiState.collectAsState()

            RoleSelectionScreen(
                newUserState = newUserState,
                navigateNext = { navController.navigate(AuthScreen.SignUp.route) }
            )
        }

        composable(route = AuthScreen.SignUp.route) {
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val newUserState by authViewModel.newUserUiState.collectAsState()

            NameInputScreen(
                newUserState = newUserState,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME.route)
                }
            )
        }
    }
}
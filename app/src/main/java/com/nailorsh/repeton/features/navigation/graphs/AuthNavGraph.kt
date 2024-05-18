package com.nailorsh.repeton.features.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.auth.presentation.ui.signin.PhoneLoginUI
import com.nailorsh.repeton.features.auth.presentation.ui.signup.RoleSelectionScreen
import com.nailorsh.repeton.features.auth.presentation.ui.signup.SignUpScreen
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
                },
                onVKAuth = { token ->
                    authViewModel.onVKAuth(token)
                    navController.popBackStack()
                    navController.navigate(Graph.HOME.route)
                }
            )
        }

        composable(route = AuthScreen.RoleSelection.route) {
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

            RoleSelectionScreen(
                setCanBeTutor = authViewModel::updateCanBeTutor,
                navigateNext = { navController.navigate(AuthScreen.SignUp.route) }
            )
        }

        composable(route = AuthScreen.SignUp.route) {
            val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
            val registrationUiState = authViewModel.registrationUiState
            val newUserState by authViewModel.newUserUiState.collectAsState()

            SignUpScreen(
                registrationUiState = registrationUiState,
                newUserState = newUserState,
                setNameAndSurname = authViewModel::updateNameAndSurname,
                retryAction = authViewModel::registerNewUser,
                onCompleted = authViewModel::registerNewUser,
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME.route)
                }
            )
        }
    }
}
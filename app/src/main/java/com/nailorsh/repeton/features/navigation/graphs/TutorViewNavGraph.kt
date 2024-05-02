package com.nailorsh.repeton.features.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nailorsh.repeton.common.data.models.UserId
import com.nailorsh.repeton.core.navigation.sharedViewModel
import com.nailorsh.repeton.features.navigation.routes.Graph
import com.nailorsh.repeton.features.navigation.routes.TutorViewScreen
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.TutorProfileScreen
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel

fun NavGraphBuilder.tutorViewNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.TUTOR_VIEW.route,
        startDestination = TutorViewScreen.TutorView.route
    ) {
        composable(route = TutorViewScreen.TutorView.route) { backStackEntry ->
            backStackEntry.arguments?.getString(TutorViewScreen.TutorView.ID_PARAM)?.let { id ->
                val viewModel = backStackEntry.sharedViewModel<TutorProfileViewModel>(navController)

                TutorProfileScreen(
                    tutorId = UserId(id),
                    onBackClicked = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}
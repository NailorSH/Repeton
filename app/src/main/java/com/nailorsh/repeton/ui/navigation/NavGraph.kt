package com.nailorsh.repeton.ui.navigation

//import com.nailorsh.repeton.domain.RepetonViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nailorsh.repeton.domain.AuthViewModel
import com.nailorsh.repeton.domain.RepetonViewModel
import com.nailorsh.repeton.ui.screens.ChatsScreen
import com.nailorsh.repeton.ui.screens.LessonScreen
import com.nailorsh.repeton.ui.screens.ProfileScreen
import com.nailorsh.repeton.ui.screens.ScheduleScreen
import com.nailorsh.repeton.ui.screens.SearchScreen
import com.nailorsh.repeton.ui.screens.auth.PhoneLoginUI

@Composable
fun NavGraph(
    navHostController: NavHostController,
    repetonViewModel: RepetonViewModel,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = AppSections.AUTH.route,
        modifier = modifier
    ) {
        composable(AppSections.AUTH.route) {
            PhoneLoginUI(
                viewModel = authViewModel,

                /*
                    TODO исправить баг с кнопкой назад
                 */
                popUpScreen = {
                    val route = AppSections.HOME.route

                    navHostController.popBackStack(
                        route = route,
                        inclusive = false,
                    )

                    navHostController.navigate(route)
                }
            )
        }
        composable(AppSections.SEARCH.route) {
            SearchScreen(
                getSearchResults = repetonViewModel::getTutors,
                typingGetSearchResults = repetonViewModel::typingTutorSearch,
//                getSearchResults = viewModel::getTutors,
                searchUiState = repetonViewModel.searchUiState
            )
        }
        composable(AppSections.HOME.route) {
            ScheduleScreen(
                onLessonClicked = { lessonId ->
                    navHostController.navigate("lesson/$lessonId")
                }
            )
        }
        composable(AppSections.CHATS.route) {
            ChatsScreen(
                getChats = repetonViewModel::getChats,
                chatsUiState = repetonViewModel.chatsUiState
            )
        }
        composable(AppSections.PROFILE.route) {
            ProfileScreen()
        }

        composable(AppSections.LESSON.route) { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                LessonScreen(lessonId = id.toInt())
            }
        }
    }
}
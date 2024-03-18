package com.nailorsh.repeton.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.domain.AuthViewModel
import com.nailorsh.repeton.domain.RepetonViewModel
import com.nailorsh.repeton.ui.navigation.AppSections
import com.nailorsh.repeton.ui.navigation.NavGraph
import com.nailorsh.repeton.ui.navigation.RepetonBottomBar
import com.nailorsh.repeton.ui.theme.RepetonTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RepetonApp(
    navController: NavHostController = rememberNavController(),
    repetonViewModel: RepetonViewModel = viewModel(factory = RepetonViewModel.Factory),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    RepetonTheme {
        Scaffold(
            bottomBar = {
                RepetonBottomBar(
                    tabs = listOf(
                        AppSections.SEARCH,
                        AppSections.HOME,
                        AppSections.CHATS,
                        AppSections.PROFILE
                    ).toTypedArray(),

                    navController = navController
                )
            }
        ) { innerPaddingModifier ->
            NavGraph(
                navHostController = navController,
                repetonViewModel = repetonViewModel,
                authViewModel = authViewModel,
                modifier = Modifier.padding(innerPaddingModifier)
            )
        }
    }

}
package com.nailorsh.repeton.features.navigation.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.navigation.graphs.RootNavGraph
import com.nailorsh.repeton.features.navigation.presentation.viewmodel.MainUiState
import com.nailorsh.repeton.features.navigation.presentation.viewmodel.MainViewModel

@Composable
fun RepetonApp() {
    val viewModel: MainViewModel = hiltViewModel()
    val mainUiState = viewModel.uiState.collectAsState()

    when (val currentState = mainUiState.value) {
        is MainUiState.Error -> {}
        is MainUiState.Loading -> {}

        is MainUiState.Success -> {
            RepetonTheme(darkTheme = currentState.isDarkThemeEnabled) {
                RootNavGraph(rememberNavController())
            }
        }
    }
}
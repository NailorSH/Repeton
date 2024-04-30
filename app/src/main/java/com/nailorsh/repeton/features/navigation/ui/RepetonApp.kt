package com.nailorsh.repeton.features.navigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.navigation.graphs.RootNavGraph

@Composable
fun RepetonApp() {

    RootNavGraph(rememberNavController())

}
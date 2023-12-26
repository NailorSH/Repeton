package com.nailorsh.repeton.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nailorsh.repeton.ui.navigation.AppSections
import com.nailorsh.repeton.ui.navigation.NavGraph
import com.nailorsh.repeton.ui.navigation.RepetonBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RepetonApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            RepetonBottomBar(
                tabs = AppSections.values(),
                navController = navController
            )
        }
    ) { innerPaddingModifier ->
        NavGraph(
            navHostController = navController,
            modifier = Modifier.fillMaxSize().padding(innerPaddingModifier)
        )
    }
}
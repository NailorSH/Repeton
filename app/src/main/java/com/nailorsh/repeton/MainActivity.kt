package com.nailorsh.repeton

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.nailorsh.repeton.MainActivity.Companion.mainActivity
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.navigation.ui.RepetonApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        var mainActivity: MainActivity? = null
        fun getInstance(): MainActivity? = mainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivity = this
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val mainUiState = viewModel.uiState.collectAsState()
            when (val currentState = mainUiState.value) {
                is MainUiState.Error -> {}
                is MainUiState.Loading -> {}

                is MainUiState.Success -> {
                    Log.d("MAIN", currentState.isDarkThemeEnabled.toString())
                    RepetonTheme(darkTheme = currentState.isDarkThemeEnabled) {
                        RepetonApp()
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        mainActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity = null
    }
}
package com.nailorsh.repeton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nailorsh.repeton.ui.RepetonApp
import com.nailorsh.repeton.ui.theme.RepetonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RepetonTheme {
                RepetonApp()
            }
        }
    }
}
package com.nailorsh.repeton.features.auth.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nailorsh.repeton.R
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthViewModel
import com.nailorsh.repeton.features.auth.presentation.viewmodel.Response

@Composable
fun PhoneLoginUI(
    popUpScreen: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(),
    restartLogin: () -> Unit = { viewModel.signUpState.value = Response.NotInitialized }
) {
    val context = LocalContext.current

    // Sign up state
    val uiState by viewModel.signUpState
        .collectAsState(initial = Response.NotInitialized)

    // SMS code 
    val code by viewModel.code.collectAsState(initial = "")

    // Phone number
    val phone by viewModel.number.collectAsState(initial = "")

    val focusManager = LocalFocusManager.current

    when (uiState) {
        // Nothing happening yet
        is Response.NotInitialized -> {
            EnterPhoneNumberUI(
                modifier = Modifier
                    .padding(vertical = 56.dp, horizontal = 24.dp),
                onClick = {
                    focusManager.clearFocus()
                    viewModel.authenticatePhone(phone)
                },
                phone = phone,
                onPhoneChange = viewModel::onNumberChange,
                onDone = {
                    focusManager.clearFocus()
                    viewModel.authenticatePhone(phone)
                }
            )
        }

        // State loading
        is Response.Loading -> {
            val text = (uiState as Response.Loading).message
            if (text == context.getString(R.string.code_sent)) {

                // If the code is sent, display the screen for code
                EnterCodeUI(
                    code = code,
                    onCodeChange = viewModel::onCodeChange,
                    phone = phone,
                    onGo = {
                        Log.d("Code Sent", "The code is $code")
                        focusManager.clearFocus()
                        viewModel.verifyOtp(code)
                    })

            } else {
                // If the loading state is different form the code sent state,
                // show a progress indicator
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    text?.let {
                        Text(
                            it, modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }


            }

        }

        // If it is the error state, show the error UI
        is Response.Error -> {
            val throwable = (uiState as Response.Error).exception!!
            ErrorUi(exc = throwable, onRestart = restartLogin)
        }

        // You can navigate when the auth process is successful
        is Response.Success -> {
            Log.d("Code", "The Sign in was successful")
            popUpScreen()
        }

    }


}
package com.nailorsh.repeton.features.auth.presentation.ui.signup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.features.auth.data.model.UserData
import com.nailorsh.repeton.features.auth.presentation.viewmodel.RegistrationUiState

@Composable
fun SignUpScreen(
    registrationUiState: RegistrationUiState,
    newUserState: UserData,
    setNameAndSurname: (String, String) -> Unit,
    retryAction: () -> Unit,
    onCompleted: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        when (registrationUiState) {
            is RegistrationUiState.NotInitialized -> NameInputScreen(
                newUserState = newUserState,
                setNameAndSurname = setNameAndSurname,
                onCompleted = onCompleted
            )

            is RegistrationUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
            is RegistrationUiState.Success -> {
                navigateToHome()
            }

            is RegistrationUiState.Error -> ErrorScreen(
                retryAction = retryAction,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}
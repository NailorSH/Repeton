package com.nailorsh.repeton.features.auth.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.features.auth.data.AuthRepository
import com.nailorsh.repeton.features.auth.data.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object NotInitialized : AuthUiState()
    class Loading(val message: String?) : AuthUiState()
    class Success(val message: String?) : AuthUiState()
    class Error(val exception: Throwable?) : AuthUiState()
}

sealed class RegistrationUiState {
    class Loading(val message: String?) : RegistrationUiState()
    class Success(val message: String?) : RegistrationUiState()
    class Error(val exception: Throwable?) : RegistrationUiState()
    object NotInitialized : RegistrationUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthRepository
) : ViewModel() {
    val authUiState: MutableStateFlow<AuthUiState> = authService.authState

    private val _newUserUIState = MutableStateFlow(UserData())
    val newUserUIState: StateFlow<UserData> = _newUserUIState.asStateFlow()

    var registrationUiState: RegistrationUiState by mutableStateOf(RegistrationUiState.NotInitialized)
        private set

    private val _number: MutableStateFlow<String> = MutableStateFlow("")
    val number: StateFlow<String> get() = _number

    private val _code: MutableStateFlow<String> = MutableStateFlow("")
    val code: StateFlow<String> get() = _code

    fun authenticatePhone(phone: String) {
        authService.authenticate(phone)
    }

    fun onNumberChange(number: String) {
        _number.value = number
    }

    fun onCodeChange(code: String) {
        _code.value = code.take(6)
    }

    fun verifyOtp(code: String) {
        viewModelScope.launch {
            authService.onVerifyOtp(code)
        }
    }

    fun createAnonymousAccount() {
        viewModelScope.launch {
            authService.createAnonymousAccount()
        }
    }

    fun registerNewUser() {

    }
}
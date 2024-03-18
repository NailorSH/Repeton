package com.nailorsh.repeton.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nailorsh.repeton.RepetonApplication
import com.nailorsh.repeton.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthState(
    val isAuthSuccessful: Boolean = false
)
class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun onAuthResult(phoneNumber: String, otp: String) {
        if (phoneNumber.isNotEmpty()) {
            authRepository.sendVerificationCode(phoneNumber)
        }
        if (otp.isNotEmpty()) {
            authRepository.verifyOtp(otp)
            _state.update { it.copy(
                isAuthSuccessful = true,
            ) }
        }
    }

    fun resetState() {
        _state.update { AuthState() }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RepetonApplication)
                val authRepository = application.container.authRepository
                AuthViewModel(authRepository = authRepository)
            }
        }
    }
}
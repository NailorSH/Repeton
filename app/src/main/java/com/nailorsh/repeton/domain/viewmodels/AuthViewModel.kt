package com.nailorsh.repeton.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.domain.repositories.AuthServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Response {
    object NotInitialized : Response()
    class Loading(val message: String?) : Response()
    class Success(val message: String?) : Response()
    class Error(val exception: Throwable?) : Response()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthServiceRepository
) : ViewModel() {
    val signUpState: MutableStateFlow<Response> = authService.signUpState

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
}
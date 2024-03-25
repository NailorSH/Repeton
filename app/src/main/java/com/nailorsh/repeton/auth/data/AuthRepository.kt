package com.nailorsh.repeton.auth.data

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.nailorsh.repeton.auth.presentation.viewmodel.Response
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {
    val signUpState: MutableStateFlow<Response>
    fun authenticate(phone: String)
    fun onCodeSent(
        verificationId: String,
        token:
        PhoneAuthProvider.ForceResendingToken
    )

    fun onVerifyOtp(code: String)
    fun onVerificationCompleted(credential: PhoneAuthCredential)
    fun onVerificationFailed(exception: Exception)
    fun getUserPhone(): String
    fun getUserId(): String
}
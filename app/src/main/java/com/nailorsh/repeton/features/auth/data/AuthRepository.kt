package com.nailorsh.repeton.features.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.nailorsh.repeton.features.auth.data.model.UserData
import com.nailorsh.repeton.features.auth.presentation.viewmodel.AuthState
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {
    val authState: MutableStateFlow<AuthState>
    fun authenticate(phone: String)
    suspend fun register(user: UserData)
    fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    )
    fun onVerifyOtp(code: String)
    fun onVerificationCompleted(credential: PhoneAuthCredential)
    fun onVerificationFailed(exception: Exception)
    fun getUserPhone(): String
    fun getUserId(): String
    fun checkUserExists(onComplete: (Boolean) -> Unit)
    suspend fun createAnonymousAccount()
    fun isUserAuthorized(): Boolean
    fun addAuthStateListener(listener: (FirebaseAuth) -> Unit)
}
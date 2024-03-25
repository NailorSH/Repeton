package com.nailorsh.repeton.auth.data

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nailorsh.repeton.MainActivity
import com.nailorsh.repeton.R
import com.nailorsh.repeton.auth.presentation.viewmodel.Response
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthServiceRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val context: MainActivity
) : AuthServiceRepository {
    private val TAG = AuthServiceRepository::class.java.simpleName

    var verificationOtp: String = ""
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null
    override var signUpState: MutableStateFlow<Response> =
        MutableStateFlow(Response.NotInitialized)
        private set

    private val authCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.i(
                TAG,
                "onVerificationCompleted: Verification completed. ${context.getString(R.string.verification_complete)}"
            )
            signUpState.value =
                Response.Loading(message = context.getString(R.string.verification_complete))

            // Use obtained credential to sign in user
            signInWithAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    signUpState.value = Response.Error(
                        exception = Exception(context.getString(R.string.verification_failed_try_again))
                    )
                }

                is FirebaseTooManyRequestsException -> {
                    signUpState.value = Response.Error(
                        exception = Exception(context.getString(R.string.quota_exceeded))
                    )
                }

                else -> {
                    signUpState.value = Response.Error(exception)
                }
            }
        }

        override fun onCodeSent(
            code: String, token:
            PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(code, token)
            verificationOtp = code
            resentToken = token
            signUpState.value = Response.Loading(
                message = context.getString(R.string.code_sent)
            )
        }

    }

    private val authBuilder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(context)
        .setTimeout(120L, TimeUnit.SECONDS)

    private fun signInWithAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "signInWithAuthCredential:The sign in succeeded ")
                    signUpState.value = Response.Success(
                        message = context.getString(R.string.phone_auth_success)
                    )
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e(TAG, context.getString(R.string.invalid_code))
                        signUpState.value = Response.Error(
                            exception = Exception(context.getString(R.string.invalid_code))
                        )

                        return@addOnCompleteListener
                    } else {
                        signUpState.value = Response.Error(task.exception)
                        Log.e(TAG, "signInWithAuthCredential: Error ${task.exception?.message}")

                    }
                }
            }
    }

    override fun getUserId(): String {
        return auth.currentUser?.uid.orEmpty()
    }

    override fun authenticate(phone: String) {
        signUpState.value =
            Response.Loading("${context.getString(R.string.code_will_be_sent)} $phone")

        val options = authBuilder
            .setPhoneNumber(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)
    }

    override fun onVerifyOtp(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, code)
        signInWithAuthCredential(credential)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        authCallbacks.onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(exception: Exception) {
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }

    override fun getUserPhone(): String {
        return auth.currentUser?.phoneNumber.orEmpty()
    }
}
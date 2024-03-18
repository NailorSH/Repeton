package com.nailorsh.repeton.network

import android.app.Activity
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.DefaultAppContainer
import java.util.concurrent.TimeUnit

class FirebasePhoneAuthClient(
    private val context: DefaultAppContainer,
    private val mAuth: FirebaseAuth
) {
    var verificationOtp = ""
    val turnOffPhoneVerify = FirebaseAuth.getInstance().firebaseAuthSettings
        .setAppVerificationDisabledForTesting(false)

    fun sendVerificationCode(mobileNum: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mobileNum)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context as Activity)
            .setCallbacks(object :
                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    verificationOtp = otp
                    Toast.makeText(context, "Otp Send Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, otp)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.verification_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.wrong_otp), Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
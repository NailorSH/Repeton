package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.network.FirebasePhoneAuthClient

interface AuthRepository {
    fun sendVerificationCode(phoneNumber: String)
    fun verifyOtp(otp: String)
}

class DefaultAuthRepository(
    private val authClient: FirebasePhoneAuthClient
) : AuthRepository {
    override fun sendVerificationCode(
        phoneNumber: String
    ) = authClient.sendVerificationCode(phoneNumber)

    override fun verifyOtp(otp: String) = authClient.verifyOtp(otp)
}
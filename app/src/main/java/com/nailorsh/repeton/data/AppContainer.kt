package com.nailorsh.repeton.data

import com.google.firebase.auth.FirebaseAuth
import com.nailorsh.repeton.data.repositories.AuthRepository
import com.nailorsh.repeton.data.repositories.DefaultAuthRepository
import com.nailorsh.repeton.data.repositories.FakeRepetonRepository
import com.nailorsh.repeton.data.repositories.RepetonRepository
import com.nailorsh.repeton.network.FirebasePhoneAuthClient

interface AppContainer {
    val authRepository: AuthRepository
    val repetonRepository: RepetonRepository
}

class DefaultAppContainer : AppContainer {
    private val mAuth = FirebaseAuth.getInstance()
    private val authClient = FirebasePhoneAuthClient(this, mAuth)

    override val authRepository: AuthRepository by lazy {
        DefaultAuthRepository(authClient)
    }


    override val repetonRepository: RepetonRepository by lazy {
        FakeRepetonRepository()
    }
}
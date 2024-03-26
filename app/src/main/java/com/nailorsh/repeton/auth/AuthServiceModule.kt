package com.nailorsh.repeton.auth

import com.nailorsh.repeton.auth.data.AuthRepository
import com.nailorsh.repeton.auth.data.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthServiceModule {
    @Binds
    abstract fun bindAuthService(
        authServiceRepositoryImpl: FirebaseAuthRepository
    ): AuthRepository
}
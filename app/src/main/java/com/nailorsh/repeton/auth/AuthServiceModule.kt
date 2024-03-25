package com.nailorsh.repeton.auth

import com.nailorsh.repeton.auth.data.AuthServiceRepository
import com.nailorsh.repeton.auth.data.FirebaseAuthServiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthServiceModule {
    @Binds
    abstract fun bindAuthService(
        authServiceRepositoryImpl: FirebaseAuthServiceRepository
    ): AuthServiceRepository
}
package com.nailorsh.repeton.di

import com.nailorsh.repeton.data.repositories.FirebaseAuthServiceRepository
import com.nailorsh.repeton.domain.repositories.AuthServiceRepository
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
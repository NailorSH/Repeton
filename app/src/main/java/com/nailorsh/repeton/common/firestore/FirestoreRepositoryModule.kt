package com.nailorsh.repeton.common.firestore

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirestoreRepositoryModule {
    @Binds
    abstract fun bindNewFirestoreRepository(
        fireRepositoryImpl: FirestoreRepositoryImpl
    ): FirestoreRepository
}
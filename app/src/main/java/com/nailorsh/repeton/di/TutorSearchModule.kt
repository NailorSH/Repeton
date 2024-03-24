package com.nailorsh.repeton.di

import com.nailorsh.repeton.data.repositories.FakeTutorSearchRepository
import com.nailorsh.repeton.domain.repositories.TutorSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TutorSearchModule {
    @Binds
    abstract fun bindTutorSearchRepository(
        tutorSearchRepositoryImpl: FakeTutorSearchRepository
    ): TutorSearchRepository
}
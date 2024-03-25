package com.nailorsh.repeton.di

import com.nailorsh.repeton.data.repositories.FakeCurrentLessonRepository
import com.nailorsh.repeton.domain.repositories.CurrentLessonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrentLessonModule {
    @Binds
    abstract fun bindCurrentLessonRepository(
        currentLessonRepositoryImpl: FakeCurrentLessonRepository
    ): CurrentLessonRepository
}
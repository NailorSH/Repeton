package com.nailorsh.repeton.features.currentlesson

import com.nailorsh.repeton.features.currentlesson.data.CurrentLessonRepository
import com.nailorsh.repeton.features.currentlesson.data.CurrentLessonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrentLessonModule {
    @Binds
    abstract fun bindCurrentLessonRepository(
        currentLessonRepositoryImpl: CurrentLessonRepositoryImpl
    ): CurrentLessonRepository
}
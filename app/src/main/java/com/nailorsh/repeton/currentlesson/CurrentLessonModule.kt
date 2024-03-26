package com.nailorsh.repeton.currentlesson

import com.nailorsh.repeton.currentlesson.data.CurrentLessonRepository
import com.nailorsh.repeton.currentlesson.data.FakeCurrentLessonRepository
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
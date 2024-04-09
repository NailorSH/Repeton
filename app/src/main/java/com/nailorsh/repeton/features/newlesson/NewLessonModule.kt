package com.nailorsh.repeton.features.newlesson

import com.nailorsh.repeton.features.newlesson.data.FakeNewLessonRepository
import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NewLessonModule {
    @Binds
    abstract fun bindNewLessonRepository(
        newLessonRepositoryImpl: FakeNewLessonRepository
    ): NewLessonRepository
}
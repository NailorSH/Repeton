package com.nailorsh.repeton.features.tutorprofile

import com.nailorsh.repeton.features.tutorprofile.data.FakeTutorProfileRepository
import com.nailorsh.repeton.features.tutorprofile.data.TutorProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TutorProfileModule {
    @Binds
    abstract fun bindTutorProfileRepository(
        tutorProfileRepositoryImpl: FakeTutorProfileRepository
    ): TutorProfileRepository
}
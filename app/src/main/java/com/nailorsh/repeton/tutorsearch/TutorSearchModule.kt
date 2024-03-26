package com.nailorsh.repeton.tutorsearch

import com.nailorsh.repeton.tutorsearch.data.FakeTutorSearchRepository
import com.nailorsh.repeton.tutorsearch.data.TutorSearchRepository
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
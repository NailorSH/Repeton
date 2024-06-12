package com.nailorsh.repeton.features.subjects

import com.nailorsh.repeton.features.subjects.data.SubjectsRepository
import com.nailorsh.repeton.features.subjects.data.SubjectsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SubjectsModule {
    @Binds
    abstract fun bindSubjectsRepository(
        subjectsRepositoryImpl: SubjectsRepositoryImpl
    ): SubjectsRepository
}
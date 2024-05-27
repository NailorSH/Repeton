package com.nailorsh.repeton.features.students

import com.nailorsh.repeton.features.students.data.StudentsRepository
import com.nailorsh.repeton.features.students.data.StudentsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentsModule {
    @Binds
    abstract fun bindSubjectsRepository(
        subjectsRepositoryImpl: StudentsRepositoryImpl
    ): StudentsRepository
}
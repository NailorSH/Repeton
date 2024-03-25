package com.nailorsh.repeton.di

import com.nailorsh.repeton.data.repositories.FakeScheduleRepository
import com.nailorsh.repeton.domain.repositories.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleModule {
    @Binds
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: FakeScheduleRepository
    ): ScheduleRepository
}
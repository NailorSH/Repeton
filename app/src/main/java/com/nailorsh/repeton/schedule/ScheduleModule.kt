package com.nailorsh.repeton.schedule

import com.nailorsh.repeton.schedule.data.FakeScheduleRepository
import com.nailorsh.repeton.schedule.data.ScheduleRepository
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
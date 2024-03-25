package com.nailorsh.repeton.di

import com.nailorsh.repeton.data.repositories.FakeMessengerRepository
import com.nailorsh.repeton.domain.repositories.MessengerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MessengerModule {
    @Binds
    abstract fun bindScheduleRepository(
        messengerRepositoryImpl: FakeMessengerRepository
    ): MessengerRepository
}
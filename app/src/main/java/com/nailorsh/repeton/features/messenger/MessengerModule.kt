package com.nailorsh.repeton.features.messenger

import com.nailorsh.repeton.features.messenger.data.FakeMessengerRepository
import com.nailorsh.repeton.features.messenger.data.MessengerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MessengerModule {
    @Binds
    abstract fun bindMessengerRepository(
        messengerRepositoryImpl: FakeMessengerRepository
    ): MessengerRepository
}
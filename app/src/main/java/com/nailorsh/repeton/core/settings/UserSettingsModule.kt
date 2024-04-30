package com.nailorsh.repeton.core.settings

import android.content.Context
import com.nailorsh.repeton.features.tutorprofile.data.FakeTutorProfileRepository
import com.nailorsh.repeton.features.tutorprofile.data.TutorProfileRepository
import com.nailorsh.repeton.features.userprofile.data.ActualProfileRepository
import com.nailorsh.repeton.features.userprofile.data.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun provideSettings(
        @ApplicationContext context: Context
    ): UserSettingsRepository = UserSettingsRepositoryImpl(context)

}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class UserSettingsModule {
//    @Binds
//    abstract fun userSettingsRepository(
//        userSettingsRepositoryImpl: UserSettingsRepositoryImpl
//    ): UserSettingsRepository
//}
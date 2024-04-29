package com.nailorsh.repeton.features.userprofile

import com.nailorsh.repeton.features.userprofile.data.ActualProfileRepository
import com.nailorsh.repeton.features.userprofile.data.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserProfileModule {
    @Binds
    abstract fun bindUserProfileRepository(
        userProfileRepositoryImpl: ActualProfileRepository
    ): UserProfileRepository

}
package com.nailorsh.repeton.features.userprofile.data

import com.nailorsh.repeton.features.userprofile.data.models.ProfileUserData


interface UserProfileRepository {

    suspend fun getSettingsOptions(): List<Options>
    suspend fun getUserOptions(): List<Options>
    suspend fun getUserData() : ProfileUserData
}
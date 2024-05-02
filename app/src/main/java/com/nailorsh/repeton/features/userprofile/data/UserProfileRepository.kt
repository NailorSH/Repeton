package com.nailorsh.repeton.features.userprofile.data


interface UserProfileRepository {

    suspend fun getSettingsOptions(): List<Options>
    suspend fun getTutorOptions(): List<Options>
    suspend fun getStudentOptions(): List<Options>


}
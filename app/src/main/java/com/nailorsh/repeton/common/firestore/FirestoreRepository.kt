package com.nailorsh.repeton.common.firestore

import com.nailorsh.repeton.common.firestore.models.UserDto

interface FirestoreRepository {
    suspend fun getUserType() : Boolean
    suspend fun getUserDto() : UserDto
}
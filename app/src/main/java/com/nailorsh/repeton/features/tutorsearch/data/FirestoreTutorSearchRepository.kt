package com.nailorsh.repeton.features.tutorsearch.data

import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTutorSearchRepository @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : TutorSearchRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        firestoreRepository.getTutors()
    }
}
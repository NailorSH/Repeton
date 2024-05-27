package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTutorProfileRepository @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : TutorProfileRepository {
    override suspend fun getTutorProfile(id: Id): Tutor = withContext(Dispatchers.IO) {
        firestoreRepository.getTutor(id)
            ?: throw NoSuchElementException("Tutor not found for id: ${id.value}")
    }
}
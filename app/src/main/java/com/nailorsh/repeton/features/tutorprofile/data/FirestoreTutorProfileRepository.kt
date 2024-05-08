package com.nailorsh.repeton.features.tutorprofile.data

import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.features.tutorsearch.data.toTutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTutorProfileRepository @Inject constructor(
    private val db: FirebaseFirestore
) : TutorProfileRepository {
    override suspend fun getTutorProfile(id: Id): Tutor = withContext(Dispatchers.IO) {
        val document = db.collection("users").document(id.value)
            .get()
            .await()
        if (document.exists()) {
            document.toTutor()
                ?: throw NoSuchElementException("Tutor could not be found or deserialized for id: ${id.value}")
        } else {
            throw NoSuchElementException("Tutor not found for id: ${id.value}")
        }
    }
}
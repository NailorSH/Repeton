package com.nailorsh.repeton.features.tutorprofile.data

import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.features.tutorsearch.data.toTutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTutorProfileRepository @Inject constructor(
    private val db: FirebaseFirestore
) : TutorProfileRepository {
    override suspend fun getTutorProfile(id: String): Tutor = withContext(Dispatchers.IO) {
        val document = db.collection("users").document(id).get().await()
        if (document.exists()) {
            document.toTutor() ?: throw IllegalStateException("Tutor not found")
        } else {
            throw IllegalStateException("Tutor not found")
        }
    }
}
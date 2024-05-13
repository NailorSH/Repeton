package com.nailorsh.repeton.features.tutorsearch.data

import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.data.models.user.toTutorWithId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTutorSearchRepository @Inject constructor(
    private val db: FirebaseFirestore
) : TutorSearchRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {

        val result = db.collection("users")
            .whereEqualTo("canBeTutor", true)
            .get()
            .await()

        val tutorsDeferred = result.documents.map { document ->
            async { document.toTutorWithId() }
        }

        tutorsDeferred.awaitAll()
    }
}
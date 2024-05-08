package com.nailorsh.repeton.features.tutorsearch.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.data.models.user.mapToTutorWithId
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTutorSearchRepository @Inject constructor(
    private val db: FirebaseFirestore
) : TutorSearchRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        val deferredTutorsList = CompletableDeferred<List<Tutor>>()

        db.collection("users")
            .whereEqualTo("canBeTutor", true)
            .get()
            .addOnSuccessListener { result ->
                val tutorsList = mutableListOf<Tutor>()
                for (document in result) {
                    val tutor = document.toTutor()
                        ?: throw NoSuchElementException("Tutor could not be deserialized")
                    tutorsList.add(tutor)
                }
                deferredTutorsList.complete(tutorsList)
            }
            .addOnFailureListener { exception ->
                deferredTutorsList.completeExceptionally(exception)
            }

        deferredTutorsList.await()
    }
}

fun DocumentSnapshot.toTutor(): Tutor? {
    val data = this.data ?: return null
    return mapToTutorWithId(data, Id(this.id))
}
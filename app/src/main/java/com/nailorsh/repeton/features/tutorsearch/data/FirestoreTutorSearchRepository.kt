package com.nailorsh.repeton.features.tutorsearch.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.models.mapToTutorWithId
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
                    Log.d("FIRESTORE", "${document.id} => ${document.data}")
                    val tutor = document.toTutor()
                    if (tutor != null) {
                        tutorsList.add(tutor)
                        Log.d("FIRESTORE", "Successful add of ${tutor.id}")
                    }
                }
                deferredTutorsList.complete(tutorsList)
            }
            .addOnFailureListener { exception ->
                Log.d("FIRESTORE", "Error getting documents: ", exception)
                deferredTutorsList.completeExceptionally(exception)
            }

        deferredTutorsList.await()
    }
}

fun DocumentSnapshot.toTutor(): Tutor? {
    val data = this.data ?: return null
    return mapToTutorWithId(data, this.id)
}
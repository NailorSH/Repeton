package com.nailorsh.repeton.features.newlesson.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.newlesson.data.mappers.toNewLessonUserItem
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonItem
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewLessonRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestoreRepository: FirestoreRepository
) : NewLessonRepository {
    override suspend fun getSubjects(filter: String): List<String> = withContext(Dispatchers.IO) {
        firestoreRepository.getCurrentUserSubjectsWithPrices()?.filter { subject ->
            subject.subject.name.lowercase().startsWith(filter.lowercase())
        }?.map { subject -> subject.subject.name } ?: emptyList()
    }

    override suspend fun saveNewLesson(lesson: NewLessonItem) = withContext(Dispatchers.IO) {
        val tutor = firestoreRepository.getCurrentUser()
        val studentsIds = lesson.students.map { it.id }
        var homework: Homework? = null
        if (lesson.homework != null) {
            val attachments = lesson.homework.attachments ?: emptyList()
            homework = Homework(
                authorID = tutor.id,
                text = lesson.homework.text,
                attachments = attachments
            )
        }

        val newLesson = Lesson(
            tutor = tutor,
            studentIds = studentsIds,
            topic = lesson.topic,
            subject = lesson.subject,
            description = lesson.description ?: "",
            startTime = lesson.startTime,
            endTime = lesson.endTime,
            homework = homework,
            additionalMaterials = lesson.additionalMaterials ?: ""
        )

        firestoreRepository.addLesson(newLesson)
    }

    override suspend fun getSubject(subjectName: String) = withContext(Dispatchers.IO) {
        firestoreRepository.getCurrentUserSubjectsWithPrices()?.firstOrNull { it.subject.name == subjectName }?.subject
    }

    override suspend fun getStudents(): List<NewLessonUserItem> = withContext(Dispatchers.IO) {
        firestoreRepository.getStudents().map { it.toNewLessonUserItem() }
    }

    override suspend fun uploadImages(images: List<Attachment.Image>): List<String> = withContext(Dispatchers.IO) {

        val storageRef = storage.reference

        val imageListURLs = mutableListOf<String>()
        images.forEach { image ->
            try {
                val imagesRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
                imagesRef.putFile(image.url.toUri()).await()
                val downloadUrl = imagesRef.downloadUrl.await().toString()
                imageListURLs.add(downloadUrl)
            } catch (e: Exception) {
                throw e
            }
        }
        imageListURLs
    }

    override suspend fun getUserType(): Boolean = withContext(Dispatchers.IO) {
        firestoreRepository.getCurrentUserType()
    }
    override suspend fun uploadFile(uri: Uri): String {
        /* TODO("Not yet implemented") */
        return ""
    }
}
package com.nailorsh.repeton.features.newlesson.data.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.nailorsh.repeton.common.data.sources.FakeSubjectsSource
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.common.firestore.mappers.toDto
import com.nailorsh.repeton.common.firestore.mappers.toTimestamp
import com.nailorsh.repeton.common.firestore.models.HomeworkDto
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.features.newlesson.data.mappers.toNewLessonUserItem
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonItem
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class NewLessonRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestoreRepository: FirestoreRepository
) : NewLessonRepository {
    override suspend fun getSubjects(filter: String): List<String> = withContext(Dispatchers.IO) {
        FakeSubjectsSource.getSubjects().filter { subject ->
            subject.name.lowercase().startsWith(filter.lowercase())
        }.map { subject -> subject.name }
    }

    override suspend fun saveNewLesson(lesson: NewLessonItem) = withContext(Dispatchers.IO) {
        val tutorId = firestoreRepository.getUserId()
        val studentsIds = lesson.students.map { it.id.value }
        var homeworkDto: HomeworkDto? = null
        if (lesson.homework != null) {
            val attachments = lesson.homework.attachments?.map { it.toDto() } ?: emptyList()
            homeworkDto = HomeworkDto(
                authorId = tutorId,
                text = lesson.homework.text,
                attachments = attachments
            )
        }
        val lessonDto = LessonDto(
            tutorId = tutorId,
            studentIds = studentsIds,
            topic = lesson.topic,
            description = lesson.description ?: "",
            startTime = lesson.startTime.toTimestamp(),
            endTime = lesson.endTime.toTimestamp(),
            homework = homeworkDto,
            additionalMaterials = lesson.additionalMaterials ?: ""
        )
        firestoreRepository.addLesson(lessonDto)
    }

    override suspend fun getSubject(subjectName: String) = withContext(Dispatchers.IO) {
        FakeSubjectsSource.getSubjectByName(subjectName)
    }

    override suspend fun getStudents(): List<NewLessonUserItem> = withContext(Dispatchers.IO) {
        firestoreRepository.getStudents().map { it.toNewLessonUserItem() }
    }

    override suspend fun uploadImage(image: Bitmap): String {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        // Преобразование Bitmap в массив байтов
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        return try {
            // Загрузка массива байтов в Firebase Storage
            val uploadTask = imagesRef.putBytes(data).await()
            // Получение URL загруженного изображения
            val downloadUrl = imagesRef.downloadUrl.await().toString()
            downloadUrl
        } catch (e: Exception) {
            Log.e("Firebase", "Failed to upload image", e)
            throw e
        }
    }

    override suspend fun uploadFile(uri: Uri): String {
        TODO("Not yet implemented")
    }

}
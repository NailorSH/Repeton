package com.nailorsh.repeton.features.newlesson.data.repository

import android.net.Uri
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonItem
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem

interface NewLessonRepository {

    suspend fun getSubjects(filter: String): List<String>
    suspend fun getSubject(subjectName: String): Subject?
    suspend fun saveNewLesson(lesson: NewLessonItem)
    suspend fun getStudents(): List<NewLessonUserItem>
    suspend fun uploadImage(uri: Uri): String
    suspend fun uploadFile(uri: Uri): String
}
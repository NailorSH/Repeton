package com.nailorsh.repeton.common.firestore

import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.UserDto

interface FirestoreRepository {
    suspend fun getUserId() : String
    suspend fun getUserType() : Boolean
    suspend fun getUserDto() : UserDto
    suspend fun getStudents() : List<UserDto>
    suspend fun addLesson(newLesson : LessonDto)
    suspend fun getLessons() : List<Lesson>
}
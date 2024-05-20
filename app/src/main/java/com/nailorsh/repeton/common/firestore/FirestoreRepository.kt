package com.nailorsh.repeton.common.firestore

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.UserDto

interface FirestoreRepository {
    suspend fun getUserId() : String
    suspend fun getUserType() : Boolean
    suspend fun getUserDto() : UserDto
    suspend fun getStudents() : List<UserDto>
    suspend fun addLesson(newLesson : LessonDto)
    suspend fun getLessons() : List<Lesson>
    suspend fun getLesson(id : Id) : Lesson
    suspend fun getSubjects() : List<Subject>
    suspend fun getUser(userId : Id) : UserDto
    suspend fun getSubject(id : Id) : Subject
    suspend fun getHomework(lessonId : Id) : Homework
    suspend fun sendHomeworkMessage(lessonId : Id, message : String)
}
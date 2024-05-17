package com.nailorsh.repeton.features.newlesson.data.repository

import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.user.User

interface NewLessonRepository {

    suspend fun getSubjects(filter: String): List<String>
    suspend fun getSubject(subjectName: String): Subject?
    suspend fun saveNewLesson(lesson: Lesson)
    suspend fun getStudents(): List<User>
}
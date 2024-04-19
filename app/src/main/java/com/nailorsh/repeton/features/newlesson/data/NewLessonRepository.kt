package com.nailorsh.repeton.features.newlesson.data

import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.common.data.models.Subject

interface NewLessonRepository {

    suspend fun getSubjects() : List<Subject>
    suspend fun getSubject(subjectName : String) : Subject?
    suspend fun saveNewLesson(lesson : Lesson)
}
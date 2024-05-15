package com.nailorsh.repeton.features.newlesson.data

import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject

interface NewLessonRepository {

    suspend fun getSubjects(filter : String) : List<String>
    suspend fun getSubject(subjectName : String) : Subject?
    suspend fun saveNewLesson(lesson : Lesson)
}
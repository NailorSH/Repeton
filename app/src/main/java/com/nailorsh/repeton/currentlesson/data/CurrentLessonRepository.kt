package com.nailorsh.repeton.currentlesson.data

import com.nailorsh.repeton.currentlesson.data.model.Lesson

interface CurrentLessonRepository {
    suspend fun getLesson(id: Int): Lesson

    suspend fun addLesson(lesson: Lesson)
}
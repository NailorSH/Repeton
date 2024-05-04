package com.nailorsh.repeton.features.currentlesson.data

import com.nailorsh.repeton.common.data.models.lesson.Lesson

interface CurrentLessonRepository {
    suspend fun getLesson(id: Int): Lesson

    suspend fun addLesson(lesson: Lesson)
}
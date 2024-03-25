package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Lesson

interface CurrentLessonRepository {
    suspend fun getLesson(id: Int): Lesson
}
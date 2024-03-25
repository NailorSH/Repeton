package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Lesson

interface RepetonRepository {
    suspend fun getLesson(id: Int): Lesson
}
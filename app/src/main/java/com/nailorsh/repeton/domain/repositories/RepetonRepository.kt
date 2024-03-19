package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.model.Tutor

interface RepetonRepository {
    suspend fun getTutors(): List<Tutor>
    suspend fun getLessons(): List<Lesson>
    suspend fun getChats(): List<Chat>
    suspend fun getLesson(id: Int): Lesson
}
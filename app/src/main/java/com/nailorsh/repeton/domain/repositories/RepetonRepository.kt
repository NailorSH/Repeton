package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson

interface RepetonRepository {
    suspend fun getChats(): List<Chat>
    suspend fun getLesson(id: Int): Lesson
}
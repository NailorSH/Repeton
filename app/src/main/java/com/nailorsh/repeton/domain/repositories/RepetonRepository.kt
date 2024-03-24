package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson
import java.time.LocalDate

interface RepetonRepository {
    suspend fun getLessons(): List<Lesson>
    suspend fun getLessons(day : LocalDate) : List<Lesson>
    suspend fun getChats(): List<Chat>
    suspend fun getLesson(id: Int): Lesson
}
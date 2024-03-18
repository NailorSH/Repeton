package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.data.sources.FakeChatsSource
import com.nailorsh.repeton.data.sources.FakeLessonSource
import com.nailorsh.repeton.data.sources.FakeTutorsSource
import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.model.Tutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

interface RepetonRepository {
    suspend fun getTutors(): List<Tutor>
    suspend fun getLessons(): List<Lesson>
    suspend fun getChats(): List<Chat>
    suspend fun getLesson(id: Int): Lesson
}

class FakeRepetonRepository : RepetonRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }

    override suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
        FakeLessonSource.loadLessons()
    }

    override suspend fun getChats(): List<Chat> = withContext(Dispatchers.IO) {
        FakeChatsSource.generateChatList()
    }

    override suspend fun getLesson(id: Int): Lesson = withContext(Dispatchers.IO) {
        sleep(15000)
        FakeLessonSource.loadLessons()[id]
    }
}
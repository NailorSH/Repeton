package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.data.sources.FakeChatsSource
import com.nailorsh.repeton.data.sources.FakeLessonSource
import com.nailorsh.repeton.data.sources.FakeTutorsSource
import com.nailorsh.repeton.domain.repositories.RepetonRepository
import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.model.Tutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class FakeRepetonRepository @Inject constructor() : RepetonRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }

    override suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
//        delay(5000)
        FakeLessonSource.loadLessons()
    }

    override suspend fun getLessons(day: LocalDate): List<Lesson> = withContext(Dispatchers.IO) {
//        delay(2000)
        FakeLessonSource.loadLessons().filter {
            it.startTime.year == day.year && it.startTime.dayOfYear == day.dayOfYear
        }
    }

    override suspend fun getChats(): List<Chat> = withContext(Dispatchers.IO) {
        FakeChatsSource.generateChatList()
    }

    override suspend fun getLesson(id: Int): Lesson = withContext(Dispatchers.IO) {
        delay(2000)
        FakeLessonSource.loadLessons()[id]
    }


}
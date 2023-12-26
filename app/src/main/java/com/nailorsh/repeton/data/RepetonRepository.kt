package com.nailorsh.repeton.data

import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.model.Tutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RepetonRepository {
    suspend fun getTutors(): List<Tutor>
    suspend fun getLessons(): List<Lesson>
}

class FakeRepetonRepository : RepetonRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }

    override suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
        FakeLessonSource.loadLessons()
    }
}
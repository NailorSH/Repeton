package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.data.sources.FakeLessonSource
import com.nailorsh.repeton.domain.repositories.RepetonRepository
import com.nailorsh.repeton.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeRepetonRepository @Inject constructor() : RepetonRepository {


    override suspend fun getLesson(id: Int): Lesson = withContext(Dispatchers.IO) {
        delay(2000)
        FakeLessonSource.loadLessons()[id]
    }
}
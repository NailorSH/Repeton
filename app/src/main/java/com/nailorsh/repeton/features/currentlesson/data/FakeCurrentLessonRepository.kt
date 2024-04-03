package com.nailorsh.repeton.features.currentlesson.data

import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.common.data.sources.FakeLessonSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeCurrentLessonRepository @Inject constructor() : CurrentLessonRepository {
    override suspend fun getLesson(id: Int): Lesson = withContext(Dispatchers.IO) {
        FakeLessonSource.loadLessons()[id]
    }

    override suspend fun addLesson(lesson: Lesson) = withContext(Dispatchers.IO) {
        FakeLessonSource.addLesson(lesson)
    }
}
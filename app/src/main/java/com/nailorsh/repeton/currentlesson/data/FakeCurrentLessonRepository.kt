package com.nailorsh.repeton.currentlesson.data

import com.nailorsh.repeton.currentlesson.data.model.Lesson
import com.nailorsh.repeton.currentlesson.data.source.FakeLessonSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.data.sources.FakeLessonSource
import com.nailorsh.repeton.domain.repositories.ScheduleRepository
import com.nailorsh.repeton.model.Lesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class FakeScheduleRepository @Inject constructor() : ScheduleRepository {
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
}
package com.nailorsh.repeton.schedule.data

import com.nailorsh.repeton.currentlesson.data.model.Lesson
import java.time.LocalDate

interface ScheduleRepository {
    suspend fun getLessons(): List<Lesson>
    suspend fun getLessons(day: LocalDate): List<Lesson>
}
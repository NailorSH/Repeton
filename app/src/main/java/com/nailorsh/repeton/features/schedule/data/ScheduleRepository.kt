package com.nailorsh.repeton.features.schedule.data

import com.nailorsh.repeton.common.data.models.Lesson
import java.time.LocalDate

interface ScheduleRepository {
    suspend fun getLessons(): List<Lesson>
    suspend fun getLessons(day: LocalDate): List<Lesson>
}
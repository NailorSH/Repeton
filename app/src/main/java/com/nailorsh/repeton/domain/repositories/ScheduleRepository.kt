package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Lesson
import java.time.LocalDate

interface ScheduleRepository {
    suspend fun getLessons(): List<Lesson>
    suspend fun getLessons(day: LocalDate): List<Lesson>
}
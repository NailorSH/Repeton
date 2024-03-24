package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Tutor

interface TutorSearchRepository {
    suspend fun getTutors(): List<Tutor>
}
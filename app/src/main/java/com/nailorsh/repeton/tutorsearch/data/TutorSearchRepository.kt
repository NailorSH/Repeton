package com.nailorsh.repeton.tutorsearch.data

import com.nailorsh.repeton.tutorsearch.data.model.Tutor

interface TutorSearchRepository {
    suspend fun getTutors(): List<Tutor>
}
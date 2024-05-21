package com.nailorsh.repeton.features.tutorsearch.data

import com.nailorsh.repeton.common.data.models.user.Tutor

interface TutorSearchRepository {
    suspend fun getTutors(): List<Tutor>
}
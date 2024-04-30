package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.Tutor

interface TutorProfileRepository {
    suspend fun getTutorProfile(id: String): Tutor
}
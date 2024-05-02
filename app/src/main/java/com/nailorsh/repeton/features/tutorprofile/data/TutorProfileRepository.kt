package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.models.UserId

interface TutorProfileRepository {
    suspend fun getTutorProfile(id: UserId): Tutor
}
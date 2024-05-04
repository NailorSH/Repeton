package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.data.models.user.UserId

interface TutorProfileRepository {
    suspend fun getTutorProfile(id: UserId): Tutor
}
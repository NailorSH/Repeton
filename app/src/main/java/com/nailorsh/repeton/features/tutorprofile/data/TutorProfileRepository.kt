package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor

interface TutorProfileRepository {
    suspend fun getTutorProfile(id: Id): Tutor
}
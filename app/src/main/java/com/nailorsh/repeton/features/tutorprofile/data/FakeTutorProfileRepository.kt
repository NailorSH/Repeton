package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.models.UserId
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeTutorProfileRepository @Inject constructor() : TutorProfileRepository {
    override suspend fun getTutorProfile(id: UserId): Tutor = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorById(id)
            ?: throw NoSuchElementException("Tutor could not be found for id: ${id.value}")
    }
}
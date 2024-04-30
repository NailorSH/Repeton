package com.nailorsh.repeton.features.tutorprofile.data

import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeTutorProfileRepository @Inject constructor() : TutorProfileRepository {
    override suspend fun getTutorProfile(id: String): Tutor = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()[id.toInt()]
    }
}
package com.nailorsh.repeton.features.tutorsearch.data

import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeTutorSearchRepository @Inject constructor() : TutorSearchRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }
}
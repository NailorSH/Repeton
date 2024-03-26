package com.nailorsh.repeton.tutorsearch.data

import com.nailorsh.repeton.tutorsearch.data.model.Tutor
import com.nailorsh.repeton.tutorsearch.data.source.FakeTutorsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeTutorSearchRepository @Inject constructor() : TutorSearchRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }
}
package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.data.sources.FakeTutorsSource
import com.nailorsh.repeton.domain.repositories.TutorSearchRepository
import com.nailorsh.repeton.model.Tutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeTutorSearchRepository @Inject constructor() : TutorSearchRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }
}
package com.nailorsh.repeton.data

import com.nailorsh.repeton.model.Tutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RepetonRepository {
    suspend fun getTutors(): List<Tutor>
}

class FakeRepetonRepository : RepetonRepository {
    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        FakeTutorsSource.getTutorsList()
    }
}
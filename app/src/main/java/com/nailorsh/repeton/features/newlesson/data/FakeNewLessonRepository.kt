package com.nailorsh.repeton.features.newlesson.data


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeNewLessonRepository @Inject constructor() : NewLessonRepository {
    override suspend fun getSubjects(): List<String> = withContext(Dispatchers.IO) {
        listOf(
            "Math", "PE", "Science", "Computer Science"
        )
    }

}
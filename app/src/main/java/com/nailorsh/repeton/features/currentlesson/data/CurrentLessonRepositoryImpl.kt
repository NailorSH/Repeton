package com.nailorsh.repeton.features.currentlesson.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.sources.FakeLessonSource
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentLessonRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : CurrentLessonRepository {
    override suspend fun getLesson(id: Id): Lesson = withContext(Dispatchers.IO) {
        firestoreRepository.getLesson(id)
    }

    override suspend fun addLesson(lesson: Lesson) = withContext(Dispatchers.IO) {
        FakeLessonSource.addLesson(lesson)
    }
}
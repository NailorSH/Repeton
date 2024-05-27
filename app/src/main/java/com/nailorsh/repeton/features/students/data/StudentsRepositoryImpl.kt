package com.nailorsh.repeton.features.students.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.User
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class StudentsRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : StudentsRepository {
    override suspend fun getUserStudents(): List<User>? = withContext(Dispatchers.IO) {
        firestoreRepository.getCurrentUserStudents()
    }

    override suspend fun getStudents(): List<User> = withContext(Dispatchers.IO){
        firestoreRepository.getStudents()
    }

    override suspend fun removeStudent(studentId: Id) {
        firestoreRepository.removeCurrentUserStudent(studentId.value)
    }

    override suspend fun addStudent(studentId: Id) {
        firestoreRepository.addCurrentUserStudent(studentId.value)
    }

}
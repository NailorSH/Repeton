package com.nailorsh.repeton.features.students.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.User

interface StudentsRepository {

    suspend fun getUserStudents() : List<User>?
    suspend fun getStudents() : List<User>
    suspend fun removeStudent(studentId : Id)
    suspend fun addStudent(studentId: Id)

}
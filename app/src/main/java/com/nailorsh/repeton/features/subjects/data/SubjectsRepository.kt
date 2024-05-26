package com.nailorsh.repeton.features.subjects.data

import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice

interface SubjectsRepository {

    suspend fun getSubjects() : List<Subject>
    suspend fun getUserSubjects() : List<SubjectWithPrice>?
    suspend fun updateUserSubjects()

}
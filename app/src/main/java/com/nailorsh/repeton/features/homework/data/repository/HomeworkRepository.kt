package com.nailorsh.repeton.features.homework.data.repository

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.features.homework.data.models.HomeworkItem
import com.nailorsh.repeton.features.homework.data.models.HomeworkUser

interface HomeworkRepository {

    suspend fun getHomework(id : Id) : HomeworkItem
    suspend fun getUser(id : Id) : HomeworkUser

}
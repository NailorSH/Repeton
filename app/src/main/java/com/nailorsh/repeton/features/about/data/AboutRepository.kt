package com.nailorsh.repeton.features.about.data

import com.nailorsh.repeton.features.about.data.model.AboutUpdatedData
import com.nailorsh.repeton.features.about.data.model.AboutUserData
import com.nailorsh.repeton.features.about.data.model.EducationItem
import com.nailorsh.repeton.features.about.data.model.LanguageItem

interface AboutRepository {
    suspend fun getUserData() : AboutUserData
    suspend fun updateAboutData(data : AboutUpdatedData)
    suspend fun getLanguages() : List<LanguageItem>
    suspend fun getEducation() : List<EducationItem>

}
package com.nailorsh.repeton.features.about.data

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.features.about.data.model.AboutUserData

interface AboutRepository {
    suspend fun getUserData() : AboutUserData
    suspend fun updateAbout(about : String)
    suspend fun updateEducation(education: Education)

    suspend fun updateLanguages(languages : List<Language>)

}
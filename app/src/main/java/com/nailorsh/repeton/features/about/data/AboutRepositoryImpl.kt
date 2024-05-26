package com.nailorsh.repeton.features.about.data

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.about.data.model.AboutUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AboutRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : AboutRepository {
    override suspend fun getUserData() = withContext(Dispatchers.IO) {
        val userDto = firestoreRepository.getUserDto()
        AboutUserData(
            name = userDto.name,
            surname = userDto.surname,
            photoSrc = "https://i.imgur.com/C25Otm8.jpeg",
            isTutor = userDto.canBeTutor
        )
    }

    override suspend fun updateAbout(about : String) {
//        TODO("Not yet implemented")
    }

    override suspend fun updateEducation(education: Education) {
//        TODO("Not yet implemented")
    }

    override suspend fun updateLanguages(languages: List<Language>) {
//        TODO("Not yet implemented")
    }

}
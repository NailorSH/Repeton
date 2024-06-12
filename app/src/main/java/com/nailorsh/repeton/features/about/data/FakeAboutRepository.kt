package com.nailorsh.repeton.features.about.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.about.data.model.AboutUpdatedData
import com.nailorsh.repeton.features.about.data.model.AboutUserData
import com.nailorsh.repeton.features.about.data.model.EducationItem
import com.nailorsh.repeton.features.about.data.model.LanguageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeAboutRepository @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : AboutRepository {
    override suspend fun getUserData() = withContext(Dispatchers.IO) {
        val userDto = firestoreRepository.getCurrentUserDto()
        AboutUserData(
            name = userDto.name,
            surname = userDto.surname,
            photoSrc = "https://i.imgur.com/C25Otm8.jpeg",
            isTutor = userDto.canBeTutor,
            languagesWithLevels = listOf(
                LanguageWithLevel(
                    Language(
                        id = Id("0"),
                        name = "Английский"
                    ),
                    level = LanguageLevel.OTHER
                ), LanguageWithLevel(
                    Language(
                        id = Id("1"),
                        name = "Русский"
                    ),
                    level = LanguageLevel.OTHER
                )
            ),
            education = Education(
                id = Id("0"),
                type = EducationType.BACHELOR,
                specialization = "Клоун с ИУ9"
            ),
            about = "Lorem Ipsum",
            contacts = emptyList()
        )
    }

    override suspend fun updateAboutData(data: AboutUpdatedData) {
//        TODO("Not yet implemented")
    }

    override suspend fun getLanguages(): List<LanguageItem> {
        return listOf(LanguageItem(Id("0"), "Английский"), LanguageItem(Id("1"), "Русский"))
    }

    override suspend fun getEducation(): List<EducationItem> {
        return listOf(
            EducationItem(Id("0"), "Бакалавриат"),
            EducationItem(Id("1"), "Среднее общее")
        )
    }

}
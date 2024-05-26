package com.nailorsh.repeton.features.about.data

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.about.data.model.AboutUpdatedData
import com.nailorsh.repeton.features.about.data.model.AboutUserData
import com.nailorsh.repeton.features.about.data.model.EducationItem
import com.nailorsh.repeton.features.about.data.model.LanguageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AboutRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val storage : FirebaseStorage,
) : AboutRepository {
    override suspend fun getUserData() = withContext(Dispatchers.IO) {
        val userDto = firestoreRepository.getUserDto()
        val userLanguages = firestoreRepository.getUserLanguagesWithLevels()
        val userEducation = firestoreRepository.getUserEducation()
        val userSpecialization = firestoreRepository.getUserSpecialization()
        val education = userEducation?.let {
            Education(Id("it"), it, userSpecialization)
        }
        AboutUserData(
            name = userDto.name,
            surname = userDto.surname,
            photoSrc = userDto.photoSrc,
            isTutor = userDto.canBeTutor,
            about = userDto.about,
            education = education,
            languages = userLanguages
        )
    }

    override suspend fun updateAboutData(data: AboutUpdatedData) = withContext(Dispatchers.IO) {
        data.about?.let { firestoreRepository.updateUserAbout(it) }
        data.photoSrc?.let {
            val storageRef = storage.reference
            val imagesRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
            imagesRef.putFile(it.toUri()).await()
            val downloadUrl = imagesRef.downloadUrl.await().toString()
            firestoreRepository.updatePhotoSrc(downloadUrl)
        }
//         data.languages?.let { firestoreRepository.eu }
        // data.education?.let { firestoreRepository.upda }
        return@withContext
    }


    override suspend fun getLanguages(): List<LanguageItem> = withContext(Dispatchers.IO) {
        listOf(LanguageItem(Id("0"), "Английский"), LanguageItem(Id("1"), "Русский"))
    }

    override suspend fun getEducation(): List<EducationItem> = withContext(Dispatchers.IO){
        listOf(EducationItem(Id("0"), "Бакалавриат"), EducationItem(Id("1"), "Среднее общее"))
    }

}
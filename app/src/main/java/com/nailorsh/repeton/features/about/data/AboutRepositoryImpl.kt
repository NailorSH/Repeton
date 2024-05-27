package com.nailorsh.repeton.features.about.data

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
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
        val user = firestoreRepository.getCurrentUser()
        val userLanguages = firestoreRepository.getUserLanguagesWithLevels(user.id)
        val userEducation = firestoreRepository.getUserEducations(user.id)
        val education = userEducation?.first()
        AboutUserData(
            name = user.name,
            surname = user.surname,
            photoSrc = user.photoSrc,
            isTutor = user.isTutor,
            about = user.about,
            education = education,
            languagesWithLevels = userLanguages
        )
    }

    override suspend fun updateAboutData(data: AboutUpdatedData) = withContext(Dispatchers.IO) {
        data.about?.let { firestoreRepository.updateCurrentUserAbout(it) }
        data.photoSrc?.let {
            val storageRef = storage.reference
            val imagesRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
            imagesRef.putFile(it.toUri()).await()
            val downloadUrl = imagesRef.downloadUrl.await().toString()
            firestoreRepository.updateCurrentUserPhotoSrc(downloadUrl)
        }
        data.languagesWithLevels?.forEach {
            firestoreRepository.updateCurrentUserLanguageLevel(
                languageId = it.language.id,
                level = it.level
            )
        }
         data.education?.let { firestoreRepository.updateCurrentUserEducation(education = it) }
        return@withContext
    }


    override suspend fun getLanguages(): List<LanguageItem> = withContext(Dispatchers.IO) {
        emptyList()
    }

    override suspend fun getEducation(): List<EducationItem> = withContext(Dispatchers.IO){
        val eduicationType = firestoreRepository.getEducationTypes()
        eduicationType.map { it ->
            EducationItem(id = it.id, name = it.name)
        }
    }

}
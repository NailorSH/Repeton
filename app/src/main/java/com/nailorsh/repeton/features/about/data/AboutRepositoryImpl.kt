package com.nailorsh.repeton.features.about.data

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.contact.Contact
import com.nailorsh.repeton.common.data.models.contact.ContactType
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.about.data.mappers.toEducationItem
import com.nailorsh.repeton.features.about.data.mappers.toLanguageItem
import com.nailorsh.repeton.features.about.data.model.AboutContactItem
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
    private val storage: FirebaseStorage,
) : AboutRepository {
    override suspend fun getUserData() = withContext(Dispatchers.IO) {
        val user = firestoreRepository.getCurrentUser()
        val userLanguages = firestoreRepository.getCurrentUserLanguagesWithLevels()
        val userEducation = firestoreRepository.getCurrentUserEducations()
        val contacts = firestoreRepository.getCurrentUserContacts()
        val contactItems: List<AboutContactItem> = contacts?.map { contact ->
            when (contact.type) {
                ContactType.TELEGRAM.value -> {
                    AboutContactItem.Telegram(contact.value)
                }

                ContactType.WHATSAPP.value -> {
                    AboutContactItem.WhatsApp(contact.value)
                }

                else -> {
                    AboutContactItem.Other(contact.value)
                }

            }
        } ?: listOf(AboutContactItem.Telegram(), AboutContactItem.WhatsApp())
        val education = userEducation?.first()
        AboutUserData(
            name = user.name,
            surname = user.surname,
            photoSrc = user.photoSrc,
            isTutor = user.isTutor,
            about = user.about,
            contacts = contactItems,
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
        data.languagesWithLevels?.let {
            firestoreRepository.updateCurrentUserLanguagesWithLevels(languagesWithLevels = it)
        }
        data.education?.let {
            firestoreRepository.updateCurrentUserEducations(
                educations = listOf(
                    it
                )
            )
        }
        data.contacts?.let {
            val newUserContacts = it.map { contact ->
                Contact(
                    id = Id(contact.type.value),
                    value = contact.value,
                    type = contact.type.value
                )
            }
            firestoreRepository.updateCurrentUserContacts(
                newUserContacts
            )
        }
        return@withContext
    }


    override suspend fun getLanguages(): List<LanguageItem> = withContext(Dispatchers.IO) {
        firestoreRepository.getLanguages().map { it.toLanguageItem() }
    }

    override suspend fun getEducation(): List<EducationItem> = withContext(Dispatchers.IO) {
        val educationType = firestoreRepository.getEducationTypes()
        educationType.map {
            it.toEducationItem()
        }
    }


}
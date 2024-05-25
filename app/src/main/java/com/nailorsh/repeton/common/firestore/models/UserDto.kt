package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @DocumentId val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("surname") val surname: String = "",
    @PropertyName("canBeTutor") val canBeTutor: Boolean = false,
    @PropertyName("phoneNumber") val phoneNumber: String = "",
    @PropertyName("about") val about: String? = null,
    @PropertyName("photoSrc") val photoSrc: String? = null,
    @PropertyName("languages") val languages: List<LanguageWithLevelDto>? = null,
    @PropertyName("educationId") val educationId: String? = null,
    @PropertyName("subjects") val subjects: List<SubjectWithPriceDto>? = null,
    @PropertyName("students") val students: List<String>? = null,
    @PropertyName("tutors") val tutors: List<String>? = null,
    @PropertyName("specialization") val specialization: String? = null
)  {
    companion object {
        val Anonymous = UserDto(
            id = "-1",
            name = "Anonymous",
            surname = "",
            canBeTutor = false,
            phoneNumber = "No number provided"
        )
    }
}
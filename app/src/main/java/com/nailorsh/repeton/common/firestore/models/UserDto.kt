package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @DocumentId val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("surname") val surname: String = "",
    @PropertyName("middleName") val middleName: String? = null,
    @PropertyName("canBeTutor") val canBeTutor: Boolean = false,
    @PropertyName("phoneNumber") val phoneNumber: String = "",
    @PropertyName("about") val about: String? = null,
    @PropertyName("photoSrc") val photoSrc: String? = null,
    @PropertyName("languages") val languages: List<LanguageWithLevelDto>? = null,
    @PropertyName("educations") val educations: List<EducationDto>? = null,
    @PropertyName("subjects") val subjects: List<SubjectWithPriceDto>? = null,
    @PropertyName("lessons") val lessons: List<String>? = null,
    @PropertyName("students") val students: List<String>? = null,
    @PropertyName("tutors") val tutors: List<String>? = null,
    @PropertyName("experienceYears") val experienceYears: Int = 0,
    @PropertyName("reviewsNumber") val reviewsNumber: Int = 0,
    @PropertyName("taughtLessonNumber") val taughtLessonNumber: Int = 0,
    @PropertyName("averageRating") val averageRating: Double = 0.0,
    @PropertyName("averagePrice") val averagePrice: Int = 0
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
package com.nailorsh.repeton.common.data.models.user

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.contact.Contact
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice
import com.nailorsh.repeton.common.data.models.location.Location

data class Tutor(
    override val id: Id,
    override val name: String,
    override val surname: String,
    override val middleName: String? = null,
    override val about: String? = null,
    override val photoSrc: String? = null,
    override val phoneNumber: String,
    override val location: Location? = null,
    override val isTutor: Boolean = true,
    val subjects: List<Subject>? = null,
    val educations: List<Education>? = null,
    val subjectsPrices: List<SubjectWithPrice>? = null,
    val averagePrice: Int = 0,
    val rating: Double = 0.0,
    val reviewsNumber: Int = 0,
    val taughtLessonNumber: Int = 0,
    val experienceYears: Int = 0,
    val languagesWithLevels: List<LanguageWithLevel>? = null,
    val contacts: List<Contact>? = null,
) : User {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name$surname",
            "$name $surname",
            "${name.first()} ${surname.first()}"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
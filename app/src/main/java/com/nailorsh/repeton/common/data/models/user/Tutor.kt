package com.nailorsh.repeton.common.data.models.user

import com.google.firebase.firestore.DocumentSnapshot
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice
import com.nailorsh.repeton.common.data.models.location.Location

data class Tutor(
    override val id: Id,
    override val name: String = "",
    override val surname: String = "",
    override val middleName: String? = null,
    override val about: String? = null,
    override val photoSrc: String? = null,
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
    val languages: List<Language>? = null,
) : User

// TODO убрать этот метод
fun DocumentSnapshot.toTutorWithId(): Tutor {
    val id = Id(id)  // Assuming 'id' comes from DocumentSnapshot's intrinsic property
    val name = getString("name") ?: ""
    val surname = getString("surname") ?: ""
    val middleName = getString("middleName")
    val about = getString("about")
    val photoSrc = getString("photoSrc")
    val education = getString("education")

    // TODO - сделать Map-у из Subject и Int
    val subjectsPrices = (get("subjectsPrices") as? Map<*, *>)?.mapNotNull { (key, value) ->
        (key as? String)?.let { k ->
            (value as? String)?.let { v -> k to v } // Convert value to String, pair them if not null
        }
    }?.toMap()

    val averagePrice = getLong("averagePrice")?.toInt() ?: 0
    val rating = getDouble("rating") ?: 0.0
    val reviewsNumber = getLong("reviewsNumber")?.toInt() ?: 0
    val taughtLessonNumber = getLong("taughtLessonNumber")?.toInt() ?: 0
    val experienceYears = getLong("experienceYears")?.toInt() ?: 0

    // TODO - сделать Map-у из Language и Level
    val languages = (get("languages") as? Map<*, *>)?.mapNotNull { (key, value) ->
        (key as? String)?.let { k -> // Convert key to String, continue if not null
            (value as? String)?.let { v -> k to v } // Convert value to String, pair them if not null
        }
    }?.toMap()

    return Tutor(
        id = id,
        name = name,
        surname = surname,
        middleName = middleName,
        about = about,
        photoSrc = photoSrc,
        educations = null,
        subjectsPrices = null,
        averagePrice = averagePrice,
        rating = rating,
        reviewsNumber = reviewsNumber,
        taughtLessonNumber = taughtLessonNumber,
        experienceYears = experienceYears,
        languages = null
    )
}

package com.nailorsh.repeton.common.data.models.user

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.location.Location

data class Tutor(
    override val id: Id,
    override var name: String,
    override var surname: String,
    override var middleName: String? = null,
    override var about: String? = null,
    override var photoSrc: String? = null,
    override var location: Location? = null,
    val subjects: List<Subject>? = null,
    val education: String? = null,
    val subjectsPrices: Map<String, String>? = null,
    val averagePrice: Int,
    val rating: Double,
    val reviewsNumber: Int,
    val country: String? = null,
    val countryCode: String? = null,
    val taughtLessonNumber: Int,
    val experienceYears: Int,
    val languages: Map<String, String>? = null,
) : User

fun Tutor.getFlagEmoji(): String {
    val asciiOffset = 'A'.code // ASCII code for 'A'
    val emojiRegionBase = 0x1F1E6 // Base Unicode for regional indicator symbols

    return if (countryCode != null && countryCode.length >= 2) {
        val firstCharEmojiCode =
            Character.codePointAt(countryCode, 0) - asciiOffset + emojiRegionBase
        val secondCharEmojiCode =
            Character.codePointAt(countryCode, 1) - asciiOffset + emojiRegionBase

        String(Character.toChars(firstCharEmojiCode)) + String(Character.toChars(secondCharEmojiCode))
    } else {
        // Return the default flag emoji if countryCode is null or not valid
        "🇩🇩"
    }
}

fun mapToTutorWithId(data: Map<String, Any>, id: Id): Tutor {
    return Tutor(
        id = id,
        name = data["name"] as? String ?: "",
        surname = data["surname"] as? String ?: "",
        middleName = data["middleName"] as? String,
        about = data["about"] as? String,
        photoSrc = data["photoSrc"] as? String,
        location = data["location"] as? Location,
        subjects = (data["subjects"] as? List<*>)?.filterIsInstance<Subject>(),
        education = data["education"] as? String,
        subjectsPrices = (data["subjectsPrices"] as? Map<*, *>)?.mapNotNull { (key, value) ->
            (key as? String)?.let { k ->
                (value as? String)?.let { v -> k to v } // Convert value to String, pair them if not null
            }
        }?.toMap(),
        averagePrice = (data["averagePrice"] as? Number)?.toInt() ?: 0,
        rating = (data["rating"] as? Number)?.toDouble() ?: 0.0,
        reviewsNumber = (data["reviewsNumber"] as? Number)?.toInt() ?: 0,
        country = data["country"] as? String,
        countryCode = data["countryCode"] as? String,
        taughtLessonNumber = (data["taughtLessonNumber"] as? Number)?.toInt() ?: 0,
        experienceYears = (data["experienceYears"] as? Number)?.toInt() ?: 0,
        languages = (data["languages"] as? Map<*, *>)?.mapNotNull { (key, value) ->
            (key as? String)?.let { k -> // Convert key to String, continue if not null
                (value as? String)?.let { v -> k to v } // Convert value to String, pair them if not null
            }
        }?.toMap()
    )
}

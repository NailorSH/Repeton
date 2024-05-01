package com.nailorsh.repeton.common.data.models

data class Tutor(
    override val id: UserId,
    override val name: String,
    override val surname: String,
    override val middleName: String?,
    override val about: String?,
    override val photoSrc: String?,
    val subjects: List<String>?,
    val education: String?,
    val subjectsPrices: Map<String, String>?,
    val averagePrice: Int,
    val rating: Double,
    val reviewsNumber: Int,
    val country: String?,
    val countryCode: String?,
    val taughtLessonNumber: Int,
    val experienceYears: Int,
    val languages: Map<String, String>?
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

fun mapToTutorWithId(data: Map<String, Any>, id: UserId): Tutor {
    return Tutor(
        id = id,
        name = data["name"] as? String ?: "",
        surname = data["surname"] as? String ?: "",
        middleName = data["middleName"] as? String,
        about = data["about"] as? String,
        photoSrc = data["photoSrc"] as? String,
        subjects = (data["subjects"] as? List<*>)?.filterIsInstance<String>(),
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

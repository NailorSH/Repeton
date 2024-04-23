package com.nailorsh.repeton.common.data.models

data class Tutor(
    override val id: Int,
    override val name: String,
    override val surname: String,
    override val middleName: String?,
    override val about: String?,
    override val photoSrc: String?,
    val subjects: List<String>,
    val education: String,
    val subjectsPrices: Map<String, String>,
    val averagePrice: Int,
    val rating: Double,
    val reviewsNumber: Int,
    val country: String,
    val countryCode: String,
    val taughtLessonNumber: Int,
    val experienceYears: Int,
    val languages: Map<String, String>,
) : User

fun Tutor.getFlagEmoji(): String {
    val asciiOffset = 'A'.code // ASCII code for 'A'
    val emojiRegionBase = 0x1F1E6 // Base Unicode for regional indicator symbols

    val firstCharEmojiCode = Character.codePointAt(countryCode, 0) - asciiOffset + emojiRegionBase
    val secondCharEmojiCode = Character.codePointAt(countryCode, 1) - asciiOffset + emojiRegionBase

    return String(Character.toChars(firstCharEmojiCode)) + String(
        Character.toChars(
            secondCharEmojiCode
        )
    )
}
package com.nailorsh.repeton.common.data.models

data class Tutor(
    val name: String,
    val surname: String,
    val middleName: String = "",
    val about: String = "",
    val photoSrc: String = "",
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
)

fun Tutor.getFlagEmoji(): String {
    val firstChar = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
    val secondChar = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}
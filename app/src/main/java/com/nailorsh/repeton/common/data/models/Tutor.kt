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
    val rating: Double,
    val reviewsNumber: Int
)
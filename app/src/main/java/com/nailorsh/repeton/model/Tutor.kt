package com.nailorsh.repeton.model

data class Tutor (
    val name: String,
    val surname: String,
    val middleName: String = "",
    val about: String = "",
    val photoSrc: String = "",
    val subjects: List<String>,
    val education: String,
    val subjectsPrices: Map<String, String>,
    val rating: Float,
    val reviewsNumber: Int
)
package com.nailorsh.repeton.model


import java.time.LocalDateTime

data class Lesson(
    val subject: String,
    val title: String,
    val description: String?,
    val teacherName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val homeworkLink: String?,
    val additionalMaterials: String?
)


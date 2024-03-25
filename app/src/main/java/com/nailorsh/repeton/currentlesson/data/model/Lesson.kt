package com.nailorsh.repeton.currentlesson.data.model


import java.time.LocalDateTime

data class Lesson(
    val id: Int,
    val subject: String,
    val title: String,
    val description: String?,
    val teacherName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val homeworkLink: String?,
    val additionalMaterials: String?
)


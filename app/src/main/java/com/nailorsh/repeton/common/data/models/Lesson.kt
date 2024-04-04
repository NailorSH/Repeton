package com.nailorsh.repeton.common.data.models

import java.time.LocalDateTime

data class Lesson(
    val id: Int,
    val subject: Subject,
    val title: String,
    val description: String?,
    val teacherName: String, /* TODO Заменить на ID тутора и сделать запросы данных тутора для урока */
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val homeworkLink: String?,
    val additionalMaterials: String?
)
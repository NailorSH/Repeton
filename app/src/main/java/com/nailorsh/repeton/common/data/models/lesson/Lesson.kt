package com.nailorsh.repeton.common.data.models.lesson

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor
import java.time.LocalDateTime

data class Lesson(
    val id: Id,
    val subject: Subject,
    val topic: String,
    val description: String? = null,
    val tutor: Tutor,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val homework: Homework? = null,
    val additionalMaterials: String? = null
)
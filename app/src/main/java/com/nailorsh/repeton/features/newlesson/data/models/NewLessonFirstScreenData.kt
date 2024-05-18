package com.nailorsh.repeton.features.newlesson.data.models

import com.nailorsh.repeton.common.data.models.lesson.Subject
import java.time.LocalDateTime

data class NewLessonFirstScreenData(
    val subject: Subject = Subject.None,
    val topic: String = "",
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now().plusMinutes(30),
    val students : List<NewLessonUserItem> = emptyList(),
)

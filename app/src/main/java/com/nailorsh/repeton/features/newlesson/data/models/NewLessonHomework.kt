package com.nailorsh.repeton.features.newlesson.data.models

import com.nailorsh.repeton.common.data.models.lesson.Attachment

data class NewLessonHomework(
    val text : String,
    val attachments : List<Attachment>?
)

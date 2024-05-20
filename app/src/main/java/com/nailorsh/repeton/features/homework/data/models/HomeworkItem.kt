package com.nailorsh.repeton.features.homework.data.models

import com.nailorsh.repeton.common.data.models.lesson.Attachment

data class HomeworkItem(
    val title : String,
    val description : String,
    val author : HomeworkUser,
    val images : List<Attachment.Image> = emptyList(),
    val files : List<Attachment.File> = emptyList(),
)

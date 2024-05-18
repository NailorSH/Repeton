package com.nailorsh.repeton.features.newlesson.data.models

import com.nailorsh.repeton.common.data.models.Id

data class NewLessonUserItem(
    val id : Id,
    val name : String,
    val surname : String,
    val photoSrc : String?,
)

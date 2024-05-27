package com.nailorsh.repeton.features.homework.data.models

import com.nailorsh.repeton.common.data.models.Id

data class HomeworkUser(
    val id : Id,
    val name : String,
    val surname : String,
    val photoSrc : String?,
)

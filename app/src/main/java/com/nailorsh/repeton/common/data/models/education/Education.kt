package com.nailorsh.repeton.common.data.models.education

import com.nailorsh.repeton.common.data.models.Id

data class Education(
    val id : Id,
    val name : String,
    val specialization : String? = null,
)

package com.nailorsh.repeton.common.data.models.education

import com.nailorsh.repeton.common.data.models.Id

data class Education(
    val id: Id,
    val type: EducationType,
    val specialization: String? = null
)

package com.nailorsh.repeton.common.data.models.lesson

import com.nailorsh.repeton.common.data.models.Id

data class Subject(
    val id: Id,
    val name: Map<String, String>
)

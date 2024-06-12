package com.nailorsh.repeton.common.data.models.contact

import com.nailorsh.repeton.common.data.models.Id

data class Contact(
    val id: Id,
    val value: String,
    val type: String
)

package com.nailorsh.repeton.common.data.models.lesson

import com.nailorsh.repeton.common.data.models.Id

data class Review(
    val id : Id,
    val text : String,
    val attachments : List<Attachment>
)

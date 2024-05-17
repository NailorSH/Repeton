package com.nailorsh.repeton.common.data.models.lesson


import com.nailorsh.repeton.common.data.models.Id


data class Homework(
    val text: String,
    val authorID : Id,
    val reviews : List<Review>? = null,
    val attachments: List<Attachment>? = null
)

sealed class Attachment {
    data class Image(val url: String, val description: String? = null) : Attachment()
    data class File(val url: String, val fileName: String) : Attachment()
}
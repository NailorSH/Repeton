package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.PropertyName

data class HomeworkDto(
    @PropertyName("authorId") val authorId: String = "",
    @PropertyName("text") val text: String = "",
    @PropertyName("reviews") val reviews: List<ReviewDto> = emptyList(),
    @PropertyName("attachments") val attachments: List<AttachmentDto> = emptyList()
)
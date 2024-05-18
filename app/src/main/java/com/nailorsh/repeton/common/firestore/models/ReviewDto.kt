package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class ReviewDto(
    @Exclude val id: String = "",
    @PropertyName("text") val text: String = "",
    @PropertyName("attachments") val attachments: List<AttachmentDto> = emptyList()
)

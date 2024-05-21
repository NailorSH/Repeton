package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class ReviewDto(
    @DocumentId val id: String = "",
    @PropertyName("text") val text: String = "",
    @PropertyName("attachments") val attachments: List<AttachmentDto> = emptyList()
)

package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class ContactDto(
    @DocumentId val id: String = "",
    @PropertyName("type") val type: String = "",
    @PropertyName("value") val value: String = "",
)

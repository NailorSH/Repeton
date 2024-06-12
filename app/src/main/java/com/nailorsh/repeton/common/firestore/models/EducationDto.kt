package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class EducationDto(
    @DocumentId val id: String = "",
    @PropertyName("typeId") val typeId: String = "",
    @PropertyName("specialization") val specialization: String? = null,
)

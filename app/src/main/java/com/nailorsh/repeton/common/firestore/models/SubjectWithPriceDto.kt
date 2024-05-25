package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.PropertyName

data class SubjectWithPriceDto(
    @PropertyName("subjectId") val subjectId: String = "",
    @PropertyName("price") val price: Int = 0
)

package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class SubjectDto(
    @Exclude val id : String = "",
    @PropertyName("name") val name : String = "",
)

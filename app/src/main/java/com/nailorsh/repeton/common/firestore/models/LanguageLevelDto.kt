package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class LanguageLevelDto(
    @DocumentId val id: String = "",
    @PropertyName("level") val name: String = ""
)

package com.nailorsh.repeton.common.firestore.models

import com.google.firebase.firestore.PropertyName

data class LanguageWithLevelDto(
    @PropertyName("languageId") val languageId: String = "",
    @PropertyName("levelId") val levelId: String = ""
)

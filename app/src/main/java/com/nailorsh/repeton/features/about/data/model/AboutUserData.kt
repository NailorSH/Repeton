package com.nailorsh.repeton.features.about.data.model

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel

data class AboutUserData(
    val name : String,
    val surname : String,

    val photoSrc : String ?= null,
    val isTutor : Boolean,

    val about : String? = null,
    val education: Education? = null,
    val languagesWithLevels: List<LanguageWithLevel>? = null,
)

package com.nailorsh.repeton.features.about.data.model

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.Language

data class AboutUpdatedData(
    val about : String? = null,
    val photoSrc : String? = null,
    val education : Education? = null,
    val languages : List<Language>? = null,
)

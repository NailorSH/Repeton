package com.nailorsh.repeton.features.about.data.mappers

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.features.about.data.model.EducationItem
import com.nailorsh.repeton.features.about.data.model.LanguageItem

fun EducationItem.toDomain(): Education {
    // TODO изменить EducationItem
    return Education(
        id = this.id,
        type = EducationType.fromId(this.id),
    )
}

fun EducationType.toEducationItem(): EducationItem {
    // TODO изменить toEducationItem
    return EducationItem(
        id = this.id,
        name = this.toString()
    )
}


fun Language.toLanguageItem() : LanguageItem {
    return LanguageItem(
        id = id,
        name = name
    )
}
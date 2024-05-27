package com.nailorsh.repeton.features.about.data.mappers

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.features.about.data.model.EducationItem

fun EducationItem.toDomain(): Education {
    // TODO изменить EducationItem
    return Education(
        id = this.id,
        type = EducationType.fromId(this.id),
        specialization = this.name
    )
}

fun Education.toEducationItem(): EducationItem {
    // TODO изменить toEducationItem
    return EducationItem(
        id = this.id,
        name = this.specialization ?: "Не найдено"
    )
}
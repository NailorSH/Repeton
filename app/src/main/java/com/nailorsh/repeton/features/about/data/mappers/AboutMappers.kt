package com.nailorsh.repeton.features.about.data.mappers

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.features.about.data.model.EducationItem

fun EducationItem.toDomain() =
    Education(type = EducationType.fromId(id), specialization = name) // TODO изменить EducationItem

fun Education.toEducationItem() =
    EducationItem(
        id = type.id,
        name = specialization ?: "Не найдено"
    ) // TODO изменить toEducationItem
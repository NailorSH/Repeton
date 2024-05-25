package com.nailorsh.repeton.features.about.data.mappers

import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.features.about.data.model.EducationItem

fun EducationItem.toDomain() =
    Education(id = id, name = name)

fun Education.toEducationItem() =
    EducationItem(id = id, name = name)
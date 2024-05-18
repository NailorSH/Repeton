package com.nailorsh.repeton.features.newlesson.data.mappers

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.firestore.models.UserDto
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem

fun UserDto.toNewLessonUserItem() : NewLessonUserItem {
    return NewLessonUserItem(
        id = Id(id),
        name = name,
        surname = surname,
        photoSrc = null
    )
}
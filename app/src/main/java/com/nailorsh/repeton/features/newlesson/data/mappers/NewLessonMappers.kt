package com.nailorsh.repeton.features.newlesson.data.mappers

import com.nailorsh.repeton.common.data.models.user.Student
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem

fun Student.toNewLessonUserItem(): NewLessonUserItem {
    return NewLessonUserItem(
        id = this.id,
        name = this.name,
        surname = this.surname,
        photoSrc = this.photoSrc
    )
}

package com.nailorsh.repeton.common.data.models

import com.nailorsh.repeton.common.data.sources.FakeLessonSource
import com.nailorsh.repeton.common.data.sources.FakeLessonSource._lessons
import java.time.LocalDateTime

data class Lesson(
    // ID устанавливается при добавлении нового урока
    var id : Int = 0,
    val subject: Subject,
    val title: String,
    val description: String? = null,
    val teacherName: String, /* TODO Заменить на ID тутора и сделать запросы данных тутора для урока */
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val homeworkLink: String? = null,
    val additionalMaterials: String? = null
) {

    companion object {
        private var lessonsNum : Int = 0
    }

    init {
        id = lessonsNum
        lessonsNum++
    }
}
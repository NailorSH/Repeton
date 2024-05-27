package com.nailorsh.repeton.common.firestore.mappers

import com.google.firebase.Timestamp
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Review
import com.nailorsh.repeton.common.firestore.models.AttachmentDto
import com.nailorsh.repeton.common.firestore.models.FileDto
import com.nailorsh.repeton.common.firestore.models.HomeworkDto
import com.nailorsh.repeton.common.firestore.models.ImageDto
import com.nailorsh.repeton.common.firestore.models.LanguageWithLevelDto
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.ReviewDto
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Lesson.toDto(): LessonDto {
    return LessonDto(
        tutorId = this.tutor.id.value,
        studentIds = emptyList(),
        subjectId = this.subject.id.value,
        topic = this.topic,
        startTime = this.startTime.toTimestamp(),
        endTime = this.endTime.toTimestamp(),
        homework = this.homework?.toDto(),
        additionalMaterials = this.additionalMaterials ?: ""
    )
}

fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.toInstant(ZoneOffset.UTC)
    return Timestamp(instant.epochSecond, instant.nano)
}

fun Homework.toDto(): HomeworkDto {
    return HomeworkDto(
        authorId = this.authorID.value,
        text = this.text,
        reviews = this.reviews?.map { it.toDto() } ?: emptyList(),
        attachments = this.attachments?.map { it.toDto() } ?: emptyList()
    )
}

fun Review.toDto(): ReviewDto {
    return ReviewDto(
        text = this.text,
        attachments = if (this.attachments == null) emptyList() else this.attachments.map { it.toDto() }
    )
}

fun Attachment.toDto(): AttachmentDto {
    return when (this) {
        is Attachment.Image -> AttachmentDto(
            image = ImageDto(
                url = this.url,
                description = this.description ?: ""
            )
        )

        is Attachment.File -> AttachmentDto(
            file = FileDto(
                url = this.url,
                fileName = this.fileName
            )
        )
    }
}

fun Language.toLanguageWithLevelDto(): LanguageWithLevelDto {
    return LanguageWithLevelDto(
        languageId = id.value,
        level = level.value
    )
}
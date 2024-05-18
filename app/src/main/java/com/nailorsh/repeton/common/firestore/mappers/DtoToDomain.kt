package com.nailorsh.repeton.common.firestore.mappers

import com.google.firebase.Timestamp
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Review
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.firestore.models.AttachmentDto
import com.nailorsh.repeton.common.firestore.models.HomeworkDto
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.ReviewDto
import java.time.LocalDateTime
import java.time.ZoneId

fun ReviewDto.toDomain(): Review {
    return Review(
        id = Id(this.id),
        text = this.text,
        attachments = if (this.attachments.isEmpty()) null else this.attachments.map { it.toDomain() }
    )
}

fun HomeworkDto.toDomain(): Homework {
    return Homework(
        text = this.text,
        authorID = Id(this.authorId),
        reviews = if (this.reviews.isEmpty()) null else this.reviews.map { it.toDomain() },
        attachments = if (this.attachments.isEmpty()) null else this.attachments.map { it.toDomain() }
    )
}

fun AttachmentDto.toDomain(): Attachment {
    return when {
        this.image != null -> Attachment.Image(
            url = this.image.url,
            description = this.image.description
        )

        this.file != null -> Attachment.File(
            url = this.file.url,
            fileName = this.file.fileName
        )

        else -> throw IllegalArgumentException("Invalid attachment data")
    }
}

fun LessonDto.toDomain(
    subject : Subject,
    tutor : Tutor
): Lesson {
    return Lesson(
        id = Id(this.id),
        subject = subject,
        topic = this.topic,
        description = this.description,
        tutor = tutor,
        startTime = this.startTime.toLocalDateTime(),
        endTime = this.endTime.toLocalDateTime(),
        homework = this.homework?.toDomain(),
        additionalMaterials = this.additionalMaterials
    )
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    val instant = this.toDate().toInstant()
    return LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
}
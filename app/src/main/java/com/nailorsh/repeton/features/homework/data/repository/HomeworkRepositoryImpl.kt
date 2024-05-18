package com.nailorsh.repeton.features.homework.data.repository

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.homework.data.models.HomeworkItem
import com.nailorsh.repeton.features.homework.data.models.HomeworkUser
import javax.inject.Inject

class HomeworkRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : HomeworkRepository {
    override suspend fun getHomework(id: Id): HomeworkItem {
        val lesson = firestoreRepository.getLesson(id)

        return if (lesson.homework == null) {
            HomeworkItem(
                title = "Физика",
                description = "Люблю ботать",
                author = HomeworkUser(
                    id = Id("0"),
                    name = "Александр",
                    surname = "Коновалов",
                    photoSrc = "https://i.imgur.com/C25Otm8.jpeg"
                ),
                images = listOf(
                    Attachment.Image(
                        url = "https://i.imgur.com/FEbhLoO.jpeg",
                        description = "Мне 43 ку-ку-ку"
                    ),
                    Attachment.Image(
                        url = "https://i.imgur.com/nMoCmh3.jpeg",
                        description = "Смысл жизни мемы-мемы"
                    ),
                    Attachment.Image(
                        url = "https://i.imgur.com/JcAirqj.jpeg",
                        description = "Я оператор!!!!"
                    )
                )
            )
        } else {
            val images = mutableListOf<Attachment.Image>()
            lesson.homework.attachments?.forEach {
                if (it is Attachment.Image) {
                    images.add(it)
                }
            }
            HomeworkItem(
                title = lesson.subject.name,
                author = getUser(lesson.tutor.id),
                description = lesson.homework.text,
                images = images
            )
        }
    }

    override suspend fun getUser(id: Id): HomeworkUser {
        val user = firestoreRepository.getUser(id)
        return HomeworkUser(
            id = Id(user.id),
            name = user.name,
            surname = user.surname,
            photoSrc = "https://i.imgur.com/FEbhLoO.jpeg"
        )
    }

}
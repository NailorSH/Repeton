package com.nailorsh.repeton.features.homework.data.repository

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.features.homework.data.models.HomeworkItem
import com.nailorsh.repeton.features.homework.data.models.HomeworkUser
import javax.inject.Inject

class HomeworkRepositoryImpl @Inject constructor() : HomeworkRepository {
    override suspend fun getHomework(id: Id): HomeworkItem {
        return HomeworkItem(
            title = "Физика",
            description = "No descr",
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
    }

    override suspend fun getUser(id: Id): HomeworkUser {
        return HomeworkUser(
            id = Id("0"),
            name = "Александр",
            surname = "Коновалов",
            photoSrc = "https://i.imgur.com/C25Otm8.jpeg"
        )
    }

}
package com.nailorsh.repeton.common.data.sources

import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.models.UserId
import com.nailorsh.repeton.common.data.models.toUserId

object FakeTutorsSource {
    private val tutors = listOf(
        Tutor(
            id = "0".toUserId(),
            name = "Александр ",
            surname = "Киселёв",
            middleName = "Витальевич",
            about = "Я являюсь техническим руководителем проекта «Учу на Профи.Ру» и активно " +
                    "использую современные технологии на своих занятиях.",
            photoSrc = null,
            subjects = listOf("Математика", "Информатика"),
            education = "Окончил МФТИ, ФОПФ, два красных диплома, 2005 г.",
            subjectsPrices = mapOf(
                "Математика" to "500-1000 ₽ / 60 мин",
                "Информатика" to "800-1000 ₽ / 60 мин"
            ),
            averagePrice = 500,
            rating = 4.93,
            reviewsNumber = 100,
            country = "Россия",
            countryCode = "RU",
            taughtLessonNumber = 250,
            experienceYears = 6,
            languages = mapOf("Русский" to "Носитель")
        ),

        Tutor(
            id = "1".toUserId(),
            name = "Александр",
            surname = "Коновалов",
            middleName = "Владимирович",
            about = "Преподаватель МГТУ имени Н.Э. Баумана и программист C и Refal",
            photoSrc = null,
            subjects = listOf("Информатика", "Алгоритмы"),
            education = "",
            subjectsPrices = mapOf(
                "Информатика" to "800-1000 ₽ / 60 мин",
                "Алгоритмы" to "1000-1500 ₽ / 60 мин"
            ),
            averagePrice = 1000,
            rating = 4.0,
            reviewsNumber = 3,
            country = "Казахстан",
            countryCode = "KZ",
            taughtLessonNumber = 10,
            experienceYears = 1,
            languages = mapOf(
                "Русский" to "Носитель",
                "Английский" to "Выше среднего B2",
                "Французский" to "Средний B1"
            )
        ),

        Tutor(
            id = "2".toUserId(),
            name = "Данила",
            surname = "Посевин",
            middleName = "Павлович",
            about = "Я Данила Палыч: не заботал — пересдача! " +
                    "Люблю физику и математику, аналоговые приборы, тумблерочки и проводочки.",
            photoSrc = null,
            subjects = listOf("ООП", "Компьютерные сети", "Разработка мобильных приложений"),
            education = "2004 — Московский физико-технический институт\n" +
                    "Прикладные математика и физика",
            subjectsPrices = mapOf(
                "ООП" to "800-1500 ₽ / 60 мин",
                "Компьютерные сети" to "1000-2000 ₽ / 60 мин",
                "Разработка мобильных приложений" to "1000-2000 ₽ / 60 мин"
            ),
            averagePrice = 1500,
            rating = 4.5,
            reviewsNumber = 10,
            country = "Россия",
            countryCode = "RU",
            taughtLessonNumber = 50,
            experienceYears = 2,
            languages = mapOf(
                "Русский" to "Носитель",
                "Английский" to "Выше среднего B2",
            )
        ),

        Tutor(
            id = "3".toUserId(),
            name = "Иван",
            surname = "Иванов",
            middleName = "Иванович",
            about = null,
            photoSrc = null,
            subjects = listOf("Русский язык", "Английский язык", "Немецкий язык"),
            education = "Новгородский государственный университет имени Ярослава Мудрого, " +
                    "оператор электронно-вычислительных и вычислительных машин второго разряда" +
                    "\n2014–2015 гг.",
            subjectsPrices = mapOf(
                "Русский язык" to "500-1000 ₽ / 60 мин",
                "Английский язык" to "800-1500 ₽ / 60 мин",
                "Немецкий язык" to "800-1500 ₽ / 60 мин"
            ),
            averagePrice = 800,
            rating = 3.9,
            reviewsNumber = 19,
            country = "Россия",
            countryCode = "RU",
            taughtLessonNumber = 100,
            experienceYears = 8,
            languages = mapOf(
                "Русский" to "Носитель",
                "Английский" to "Выше среднего B2",
            )
        ),
    )

    fun getTutorsList(): List<Tutor> = tutors

    fun getTutorById(id: UserId): Tutor? = tutors.firstOrNull { it.id == id }
}
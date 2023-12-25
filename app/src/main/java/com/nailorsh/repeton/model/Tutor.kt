package com.nailorsh.repeton.model

data class Tutor(
    val name: String,
    val surname: String,
    val middleName: String = "",
    val about: String = "",
    val photoSrc: String = "",
    val subjects: List<String>,
    val education: String,
    val subjectsPrices: Map<String, String>,
    val rating: Double,
    val reviewsNumber: Int
)

val tutors = listOf(
    Tutor(
        name = "Александр ",
        surname = "Киселёв",
        middleName = "Витальевич",
        about = "Я являюсь техническим руководителем проекта «Учу на Профи.Ру» и активно " +
                "использую современные технологии на своих занятиях.",
        subjects = listOf("Математика", "Информатика"),
        education = "Окончил МФТИ, ФОПФ, два красных диплома, 2005 г.",
        subjectsPrices = mapOf(
            "Математика" to "500-1000 ₽ / 60 мин",
            "Информатика" to "800-1000 ₽ / 60 мин"
        ),
        rating = 4.93,
        reviewsNumber = 100
    ),

    Tutor(
        name = "Александр",
        surname = "Коновалов",
        middleName = "Владимирович",
        about = "Преподаватель МГТУ имени Н.Э. Баумана и программист C и Refal",
        subjects = listOf("Информатика", "Алгоритмы"),
        education = "",
        subjectsPrices = mapOf(
            "Информатика" to "800-1000 ₽ / 60 мин",
            "Алгоритмы" to "1000-1500 ₽ / 60 мин"
        ),
        rating = 4.0,
        reviewsNumber = 3
    ),

    Tutor(
        name = "Данила",
        surname = "Посевин",
        middleName = "Павлович",
        about = "Я Данила Палыч: не заботал — пересдача! " +
                "Люблю физику и математику, аналоговые приборы, тумблерочки и проводочки.",
        subjects = listOf("ООП", "Компьютерные сети", "Разработка мобильных приложений"),
        education = "2004 — Московский физико-технический институт\n" +
                "Прикладные математика и физика",
        subjectsPrices = mapOf(
            "ООП" to "800-1500 ₽ / 60 мин",
            "Компьютерные сети" to "1000-2000 ₽ / 60 мин",
            "Разработка мобильных приложений" to "1000-2000 ₽ / 60 мин"
        ),
        rating = 4.5,
        reviewsNumber = 10
    ),

    Tutor(
        name = "Иван",
        surname = "Иванов",
        middleName = "Иванович",
        about = "",
        subjects = listOf("Русский язык", "Английский язык", "Немецкий язык"),
        education = "Новгородский государственный университет имени Ярослава Мудрого, " +
                "оператор электронно-вычислительных и вычислительных машин второго разряда" +
                "\n2014–2015 гг.",
        subjectsPrices = mapOf(
            "Русский язык" to "500-1000 ₽ / 60 мин",
            "Английский язык" to "800-1500 ₽ / 60 мин",
            "Немецкий язык" to "800-1500 ₽ / 60 мин"
        ),
        rating = 3.9,
        reviewsNumber = 19
    ),
)
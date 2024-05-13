package com.nailorsh.repeton.common.data.sources

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Subject

object FakeSubjectsSource {

    private var _subjects = mutableListOf(
        Subject(Id("0"), mapOf("ru" to "Математика")),
        Subject(Id("1"), mapOf("ru" to "Русский язык")),
        Subject(Id("2"), mapOf("ru" to "Физика")),
        Subject(Id("3"), mapOf("ru" to "Химия")),
        Subject(Id("4"), mapOf("ru" to "Криминальное чтиво")),
        Subject(Id("5"), mapOf("ru" to "Тринитробезнойная кислота")),
        Subject(Id("6"), mapOf("ru" to "English Tutoring Lessons")),
        Subject(Id("7"), mapOf("ru" to "Computer Graphics and Algorithms")),
        Subject(Id("8"), mapOf("ru" to "Data Structures and Algorithms"))
    )

    fun addSubject(subject: Subject) {
        _subjects.add(subject)
    }

    fun getSubject(id: Int): Subject {
        return _subjects[id]
    }

    fun getSubjectByName(subjectName: String, locale: String = "ru"): Subject? {
        return _subjects.firstOrNull { subject ->
            subject.name[locale] == subjectName
        }
    }

    fun getSubjects() : List<Subject> {
        return _subjects.toList()
    }
}
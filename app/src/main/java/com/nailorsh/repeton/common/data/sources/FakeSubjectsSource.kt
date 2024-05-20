package com.nailorsh.repeton.common.data.sources

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Subject

object FakeSubjectsSource {

    private var _subjects = mutableListOf(
        Subject(Id("0"), "Математика"),
        Subject(Id("1"), "Русский язык"),
        Subject(Id("2"), "Физика"),
        Subject(Id("3"), "Химия"),
        Subject(Id("4"), "Криминальное чтиво"),
        Subject(Id("5"), "Тринитробезнойная кислота"),
        Subject(Id("6"), "English Tutoring Lessons"),
        Subject(Id("7"), "Computer Graphics and Algorithms"),
        Subject(Id("8"), "Data Structures and Algorithms")
    )

    fun addSubject(subject: Subject) {
        _subjects.add(subject)
    }

    fun getSubject(id: Int): Subject {
        return _subjects[id]
    }

    fun getSubjectByName(subjectName: String, locale: String = "ru"): Subject? {
        return _subjects.firstOrNull { subject ->
            subject.name == subjectName
        }
    }

    fun getSubjects() : List<Subject> {
        return _subjects.toList()
    }
}
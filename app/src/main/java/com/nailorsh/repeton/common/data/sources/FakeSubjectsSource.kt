package com.nailorsh.repeton.common.data.sources

import com.nailorsh.repeton.common.data.models.lesson.Subject

object FakeSubjectsSource {

    private var _subjects = mutableListOf<Subject>(
        Subject(0, "Математика"),
        Subject(1, "Русский язык"),
        Subject(2, "Физика"),
        Subject(3, "Химия"),
        Subject(4, "Криминальное чтиво"),
        Subject(5, "Тринитробезнойная кислота"),
        Subject(6, "English Tutoring Lessons"),
        Subject(7, "Computer Graphics and Algorithms"),
        Subject(8, "Data Structures and Algorithms")
    )

    fun addSubject(subject : Subject) {
        _subjects.add(subject)
    }

    fun getSubjects(id : Int) : Subject {
        return _subjects[id]
    }

    fun getSubjects(subjectName : String) : Subject? {
        return _subjects.firstOrNull {
            it.subjectName == subjectName
        }
    }

    fun getSubjects() : List<Subject> {
        return _subjects.toList()
    }


}
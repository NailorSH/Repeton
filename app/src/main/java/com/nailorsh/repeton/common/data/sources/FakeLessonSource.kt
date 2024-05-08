package com.nailorsh.repeton.common.data.sources

import android.util.Log
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import java.time.LocalDateTime
import kotlin.random.Random

fun getRandomStartDateTime(): LocalDateTime {

    return LocalDateTime.now()
        .plusDays(Random.nextInt(0, 1).toLong())
        .plusHours(Random.nextInt(0, 1).toLong())
}

object FakeLessonSource {


    private var _lessons = mutableListOf(
        getRandomStartDateTime().let { startTime ->
            Lesson(
                id = Id("1"),
                subject = FakeSubjectsSource.getSubjects(1),
                topic = "Algebra Basics",
                description = "Introduction to algebraic concepts.",
                tutor = FakeTutorsSource.getTutorById(Id("0")),
                startTime = startTime,
                endTime = startTime.plusMinutes(90),
                homework = Homework("http://homework.example.com/algebra"),
                additionalMaterials = "http://materials.example.com/algebra"
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                id = Id("2"),
                subject = FakeSubjectsSource.getSubjects(3),
                topic = "Kinematics",
                description = "Study of motion.",
                tutor = FakeTutorsSource.getTutorById(Id("2")),
                startTime = startTime.plusMinutes(90),
                endTime = startTime.plusMinutes(180),
                homework = null,
                additionalMaterials = "http://materials.example.com/kinematics"
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                id = Id("3"),
                subject = FakeSubjectsSource.getSubjects(5),
                topic = "The French Revolution",
                description = "A deep dive into the causes of the French Revolution.",
                tutor = FakeTutorsSource.getTutorById(Id("3")),
                startTime = startTime.plusDays(1),
                endTime = startTime.plusDays(1).plusMinutes(90),
                homework = Homework("http://homework.example.com/french-revolution"),
                additionalMaterials = null
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                id = Id("3"),
                subject = FakeSubjectsSource.getSubjects(7),
                topic = "Shakespeare's Plays",
                description = "Exploring the major plays of William Shakespeare.",
                tutor = FakeTutorsSource.getTutorById(Id("1")),
                startTime = startTime.plusDays(2),
                endTime = startTime.plusDays(2).plusMinutes(90),
                homework = null,
                additionalMaterials = "http://materials.example.com/shakespeare"
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                id = Id("4"),
                subject = FakeSubjectsSource.getSubjects(4),
                topic = "Introduction to Programming",
                description = null,
                tutor = FakeTutorsSource.getTutorById(Id("1")),
                startTime = startTime.plusDays(2).plusMinutes(90),
                endTime = startTime.plusDays(2).plusMinutes(180),
                homework = Homework("http://homework.example.com/programming"),
                additionalMaterials = null
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                id = Id("5"),
                subject = FakeSubjectsSource.getSubjects(8),
                topic = "Impressionism",
                description = "Understanding the Impressionist art movement.",
                tutor = FakeTutorsSource.getTutorById(Id("1")),
                startTime = startTime.minusDays(1),
                endTime = startTime.minusDays(1).plusMinutes(90),
                homework = null,
                additionalMaterials = "http://materials.example.com/impressionism"
            )
        }
    )


    fun loadLessons(): List<Lesson> {
        return _lessons.toList()
    }

    fun addLesson(lesson: Lesson) {
        Log.d("FAKE_LESSON", _lessons.toString())
        _lessons.add(lesson)
        Log.d("FAKE_LESSON", _lessons.toString())
    }
}
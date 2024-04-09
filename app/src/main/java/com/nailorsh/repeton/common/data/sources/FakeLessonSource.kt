package com.nailorsh.repeton.common.data.sources

import android.util.Log
import com.nailorsh.repeton.common.data.models.Homework
import com.nailorsh.repeton.common.data.models.Lesson
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
                subject = FakeSubjectsSource.getSubjects(1),
                topic = "Algebra Basics",
                description = "Introduction to algebraic concepts.",
                teacherName = "Alex Johnson",
                startTime = startTime,
                endTime = startTime.plusMinutes(90),
                homework = Homework("http://homework.example.com/algebra"),
                additionalMaterials = "http://materials.example.com/algebra"
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                subject = FakeSubjectsSource.getSubjects(3),
                topic = "Kinematics",
                description = "Study of motion.",
                teacherName = "Marie Curie",
                startTime = startTime.plusMinutes(90),
                endTime = startTime.plusMinutes(180),
                homework = null,
                additionalMaterials = "http://materials.example.com/kinematics"
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                subject = FakeSubjectsSource.getSubjects(5),
                topic = "The French Revolution",
                description = "A deep dive into the causes of the French Revolution.",
                teacherName = "Jean Valjean",
                startTime = startTime.plusDays(1),
                endTime = startTime.plusDays(1).plusMinutes(90),
                homework = Homework("http://homework.example.com/french-revolution"),
                additionalMaterials = null
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                subject = FakeSubjectsSource.getSubjects(7),
                topic = "Shakespeare's Plays",
                description = "Exploring the major plays of William Shakespeare.",
                teacherName = "Elizabeth Bennett",
                startTime = startTime.plusDays(2),
                endTime = startTime.plusDays(2).plusMinutes(90),
                homework = null,
                additionalMaterials = "http://materials.example.com/shakespeare"
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                subject = FakeSubjectsSource.getSubjects(4),
                topic = "Introduction to Programming",
                description = null,
                teacherName = "Alan Turing",
                startTime = startTime.plusDays(2).plusMinutes(90),
                endTime = startTime.plusDays(2).plusMinutes(180),
                homework = Homework("http://homework.example.com/programming"),
                additionalMaterials = null
            )
        },
        getRandomStartDateTime().let { startTime ->
            Lesson(
                subject = FakeSubjectsSource.getSubjects(8),
                topic = "Impressionism",
                description = "Understanding the Impressionist art movement.",
                teacherName = "Claude Monet",
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
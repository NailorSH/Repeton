package com.nailorsh.repeton.data

import com.nailorsh.repeton.model.Lesson
import java.time.LocalDateTime
import kotlin.random.Random


fun getRandomStartDateTime(): LocalDateTime {
    val day = Random.nextInt(1, 8) // Случайный день между 1 и 7 января
    val hour = Random.nextInt(12, 21) // Случайный час между 12:00 и 20:00
    return LocalDateTime.of(2024, 1, day, hour, 0)
}

object FakeLessonSource {
    fun loadLessons() : List<Lesson> {
        return listOf(
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    subject = "Mathematics",
                    title = "Algebra Basics",
                    description = "Introduction to algebraic concepts.",
                    teacherName = "Alex Johnson",
                    startTime = startTime,
                    endTime = startTime.plusMinutes(90),
                    homeworkLink = "http://homework.example.com/algebra",
                    additionalMaterials = "http://materials.example.com/algebra"
                )
            },
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    subject = "Physics",
                    title = "Kinematics",
                    description = "Study of motion.",
                    teacherName = "Marie Curie",
                    startTime = startTime,
                    endTime = startTime.plusMinutes(90),
                    homeworkLink = null,
                    additionalMaterials = "http://materials.example.com/kinematics"
                )
            },
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    subject = "History",
                    title = "The French Revolution",
                    description = "A deep dive into the causes of the French Revolution.",
                    teacherName = "Jean Valjean",
                    startTime = startTime,
                    endTime = startTime.plusMinutes(90),
                    homeworkLink = "http://homework.example.com/french-revolution",
                    additionalMaterials = null
                )
            },
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    subject = "Literature",
                    title = "Shakespeare's Plays",
                    description = "Exploring the major plays of William Shakespeare.",
                    teacherName = "Elizabeth Bennett",
                    startTime = startTime,
                    endTime = startTime.plusMinutes(90),
                    homeworkLink = null,
                    additionalMaterials = "http://materials.example.com/shakespeare"
                )
            },
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    subject = "Computer Science",
                    title = "Introduction to Programming",
                    description = null,
                    teacherName = "Alan Turing",
                    startTime = startTime,
                    endTime = startTime.plusMinutes(90),
                    homeworkLink = "http://homework.example.com/programming",
                    additionalMaterials = null
                )
            },
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    subject = "Art",
                    title = "Impressionism",
                    description = "Understanding the Impressionist art movement.",
                    teacherName = "Claude Monet",
                    startTime = startTime,
                    endTime = startTime.plusMinutes(90),
                    homeworkLink = null,
                    additionalMaterials = "http://materials.example.com/impressionism"
                )
            }
        )
    }
}
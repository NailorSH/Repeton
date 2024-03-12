package com.nailorsh.repeton.data

import com.nailorsh.repeton.model.Lesson
import java.time.LocalDateTime
import kotlin.random.Random


fun getRandomStartDateTime(): LocalDateTime {

    return LocalDateTime.now()
        .plusDays(Random.nextInt(1, 8).toLong())
        .plusHours(Random.nextInt(0, 24).toLong())
}

object FakeLessonSource {
    fun loadLessons() : List<Lesson> {
        return listOf(
            getRandomStartDateTime().let { startTime ->
                Lesson(
                    id = 0,
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
                    id = 1,
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
                    id = 2,
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
                    id = 3,
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
                    id = 4,
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
                    id = 5,
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
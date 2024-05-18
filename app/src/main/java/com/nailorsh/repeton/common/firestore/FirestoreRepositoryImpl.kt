package com.nailorsh.repeton.common.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.firestore.mappers.toDomain
import com.nailorsh.repeton.common.firestore.models.HomeworkDto
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.SubjectDto
import com.nailorsh.repeton.common.firestore.models.UserDto
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : FirestoreRepository {

    private var userDto: UserDto? = null
    private var subjects: List<Subject>? = null
    override suspend fun getUserDto(): UserDto {
        if (userDto == null) {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val document = db.collection("users").document(uid).get().await()
                if (document.exists()) {
                    userDto = document.toObject<UserDto>()
                } else {
                    return UserDto.Anonymous
                }
            } else {
                return UserDto.Anonymous
            }

        }
        return userDto!!
    }

    override suspend fun getSubjects(): List<Subject> {
        if (subjects == null) {
            val querySnapshot = db.collection("subjects").get().await()
            subjects = querySnapshot.documents.map { document ->
                val subject = document.toObject(SubjectDto::class.java)
                subject?.toDomain() ?: throw (IOException("Lesson not found"))
            }
        }
        return subjects!!
    }

    override suspend fun getUser(userId: Id): UserDto {
        val document = db.collection("users").document(userId.value).get().await()
        if (document.exists()) {
            val user = document.toObject(UserDto::class.java)!!
            return user
        } else {
            throw (IOException("User not found"))
        }
    }

    override suspend fun getHomework(lessonId: Id): Homework {
        val document =
            db.collection("lessons").document().collection("homework").document().get().await()
        if (document.exists()) {
            val homework = document.toObject(HomeworkDto::class.java)!!
            return homework.toDomain()
        } else throw (IOException("Homework not found"))
    }

    override suspend fun getSubject(id: Id): Subject {
        val document = db.collection("subjects").document(id.value).get().await()

        if (document.exists()) {
            val subject = document.toObject<SubjectDto>()

            return subject?.toDomain() ?: throw (IOException("Error parsing lesson"))
        } else throw (IOException("Lesson not found"))
    }

    override suspend fun getUserType(): Boolean {
        return getUserDto().canBeTutor
    }

    override suspend fun getUserId(): String {
        return getUserDto().id
    }

    override suspend fun getStudents(): List<UserDto> {
        val querySnapshot = db.collection("users").whereEqualTo("canBeTutor", false).get().await()
        if (!querySnapshot.isEmpty) {
            val students = mutableListOf<UserDto>()
            querySnapshot.documents.forEach { document ->
                val user = document.toObject(UserDto::class.java)
                if (user != null) {
                    students.add(
                        user.copy(id = document.id)
                    )
                }
            }
            return students
        } else {
            return emptyList()
        }
    }


    override suspend fun addLesson(newLesson: LessonDto) {
        // Convert Lesson to LessonDto
        val lessonDto = newLesson

        // Add lesson to "lessons" collection
        val lessonRef = db.collection("lessons").document()
//        lessonDto.id = lessonRef.id // Set the generated ID to lessonDto
        lessonRef.set(lessonDto).await()

        // Add homework if it exists
        newLesson.homework?.let { homework ->
            val homeworkRef = lessonRef.collection("homework").document()
//            homeworkDto.id = homeworkRef.id // Set the generated ID to homeworkDto
            homeworkRef.set(homework).await()

            homework.attachments.forEach { attachment ->
                val attachmentRef = homeworkRef.collection("attachments").document()
//                attachmentDto.id = attachmentRef.id // Set the generated ID to attachmentDto
                attachmentRef.set(attachment).await()
            }

        }
    }

    override suspend fun getLessons(): List<Lesson> {
        val querySnapshot = db.collection("lessons").get().await()
        val lessonsDto = querySnapshot.toObjects<LessonDto>()

        return lessonsDto.map {
            val tutor = tutorProvider(it.tutorId)
            val subject = getSubject(Id(it.subjectId))
            it.toDomain(subject, tutor)
        }
    }

    override suspend fun getLesson(id: Id): Lesson {
        val document = db.collection("lessons").document(id.value).get().await()
        if (document.exists()) {
            val lesson = document.toObject<LessonDto>()

            return lesson?.toDomain(
                subject = getSubject(Id(lesson.subjectId)),
                tutor = tutorProvider(lesson.tutorId)
            ) ?: throw (IOException("Error parsing lesson"))
        } else throw (IOException("Lesson not found"))
    }


    private suspend fun tutorProvider(id: String): Tutor {
        println()
        val userSnapshot = db.collection("users").document(id).get().await()

        val userDto = userSnapshot.toObject<UserDto>()
        return userDto?.let {
            Tutor(id = Id(id), name = it.name, surname = it.surname)
        } ?: Tutor(id = Id("-1")) // TODO обработать более умно
    }
}
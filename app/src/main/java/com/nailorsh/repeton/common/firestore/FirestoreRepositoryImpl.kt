package com.nailorsh.repeton.common.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.firestore.mappers.toDomain
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.UserDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : FirestoreRepository {

    private var userDto: UserDto? = null
    override suspend fun getUserDto(): UserDto {
        if (userDto == null) {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val document = db.collection("users").document(uid).get().await()
                if (document.exists()) {
                    userDto = document.toObject(UserDto::class.java)?.copy(id = auth.currentUser!!.uid)
                } else {
                    return UserDto.Anonymous
                }
            } else {
                return UserDto.Anonymous
            }

        }
        return userDto!!
    }

    override suspend fun getUserType(): Boolean {
        return getUserDto().canBeTutor
    }

    override suspend fun getUserId() : String {
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
        val lessonsDto = querySnapshot.documents.mapNotNull { document ->
            document.toObject(LessonDto::class.java)?.apply { id = document.id }
        }

        return lessonsDto.map {
            println()
            val tutor = tutorProvider(it.tutorId)
            it.toDomain(Subject(Id(it.subjectId), emptyMap()), tutor)
        }
    }

    private suspend fun tutorProvider(id: String): Tutor {
        println()
        val userSnapshot = db.collection("users").document(id).get().await()

        val userDto = userSnapshot.toObject(UserDto::class.java)
        return userDto?.let {
            Tutor(id = Id(id), name = it.name, surname =  it.surname)
        } ?: throw Exception("Tutor not found")
    }
}
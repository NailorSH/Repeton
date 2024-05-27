package com.nailorsh.repeton.common.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice
import com.nailorsh.repeton.common.data.models.user.Student
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.firestore.mappers.toDomain
import com.nailorsh.repeton.common.firestore.mappers.toDomainStudent
import com.nailorsh.repeton.common.firestore.mappers.toDomainTutor
import com.nailorsh.repeton.common.firestore.models.EducationTypeDto
import com.nailorsh.repeton.common.firestore.models.HomeworkDto
import com.nailorsh.repeton.common.firestore.models.LanguageDto
import com.nailorsh.repeton.common.firestore.models.LanguageLevelDto
import com.nailorsh.repeton.common.firestore.models.LessonDto
import com.nailorsh.repeton.common.firestore.models.ReviewDto
import com.nailorsh.repeton.common.firestore.models.SubjectDto
import com.nailorsh.repeton.common.firestore.models.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : FirestoreRepository {

    private var userDto: UserDto? = null
    private var subjects: List<Subject>? = null
    private var educationTypes: List<EducationType>? = null
    private var languageLevels: List<LanguageLevel>? = null

    override suspend fun sendHomeworkMessage(lessonId: Id, message: String) {
        db.collection("lessons").document(lessonId.value).collection("homework").document()
            .collection("reviews").document().set(
                ReviewDto(text = message)
            )
    }

    override suspend fun updateUserName(name: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("name", name).await()
    }

    override suspend fun updateUserSurname(surname: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("surname", surname).await()
    }

    override suspend fun updatePhotoSrc(url: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("photoSrc", url).await()
    }

    override suspend fun updateUserAbout(about: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("about", about).await()
    }

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
                val subject = document.toObject<SubjectDto>()
                subject?.toDomain() ?: throw (IOException("Lesson not found"))
            }
        }
        return subjects!!
    }

    override suspend fun getLanguageLevels(): List<LanguageLevel>? {
        if (languageLevels == null) {
            val querySnapshot = db.collection("language_level").get().await()
            languageLevels = querySnapshot.documents.map { document ->
                val languageLevel = document.toObject<LanguageLevelDto>()
                languageLevel?.toDomain() ?: throw (IOException("Lesson not found"))
            }
        }
        return languageLevels!!
    }

    override suspend fun getUser(userId: Id): UserDto {
        val document = db.collection("users").document(userId.value).get().await()
        if (document.exists()) {
            val user = document.toObject<UserDto>()!!
            return user
        } else {
            throw (IOException("User not found"))
        }
    }

    override suspend fun getHomework(lessonId: Id): Homework {
        val document =
            db.collection("lessons").document().collection("homework").document().get().await()
        if (document.exists()) {
            val homework = document.toObject<HomeworkDto>()!!
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

    override suspend fun getUserId(): String {
        return getUserDto().id
    }

    override suspend fun getUserType(): Boolean {
        return getUserDto().canBeTutor
    }

    override suspend fun getUserName(): String {
        return getUserDto().name
    }

    override suspend fun getUserSurname(): String {
        return getUserDto().surname
    }

    override suspend fun getUserPhotoSrc(): String? {
        return getUserDto().photoSrc
    }

    override suspend fun getUserAbout(): String? {
        return getUserDto().about
    }

    override suspend fun getUserStudents(): List<Student>? = withContext(Dispatchers.IO) {
        val studentsIds = getUserDto().students ?: return@withContext null

        val studentTasks = studentsIds.map { studentId ->
            async {
                db.collection("users").document(studentId).get().await()
                    .toObject<UserDto>()
                    ?.toDomainStudent()
            }
        }

        studentTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
    }

    override suspend fun addUserStudent(studentId: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val userDocRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            // Проверяем, есть ли уже такой studentId в массиве
            if (snapshot.exists()) {
                val students = snapshot.get("students") as? List<String> ?: listOf()
                if (!students.contains(studentId)) {
                    transaction.update(userDocRef, "students", FieldValue.arrayUnion(studentId))
                }
            }
            // Возвращаем результат для дальнейшего использования, если необходимо
            snapshot
        }.await()
    }

    override suspend fun removeUserStudent(studentId: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val userDocRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            // Проверяем, есть ли уже такой studentId в массиве
            if (snapshot.exists()) {
                val students = snapshot.get("students") as? List<String> ?: listOf()
                if (!students.contains(studentId)) {
                    transaction.update(userDocRef, "students", FieldValue.arrayRemove(studentId))
                }
            }
            // Возвращаем результат для дальнейшего использования, если необходимо
            snapshot
        }.await()
    }

    override suspend fun getUserTutors(): List<Tutor>? = withContext(Dispatchers.IO) {
        val tutorsIds = getUserDto().tutors ?: return@withContext null

        val tutorTasks = tutorsIds.map { tutorId ->
            async {
                db.collection("users").document(tutorId).get()
                    .await()
                    .toObject<UserDto>()
                    ?.toDomainTutor(
                        subjectsPrices = getUserSubjectsWithPrices(),
                        languages = getUserLanguagesWithLevels(),
                        educations = getUserEducations()
                    )
            }
        }

        tutorTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
    }

    override suspend fun addUserTutor(tutorId: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val userDocRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            // Проверяем, есть ли уже такой tutorId в массиве
            if (snapshot.exists()) {
                val tutors = snapshot.get("tutors") as? List<String> ?: listOf()
                if (!tutors.contains(tutorId)) {
                    transaction.update(userDocRef, "tutors", FieldValue.arrayUnion(tutorId))
                }
            }
            // Возвращаем результат для дальнейшего использования, если необходимо
            snapshot
        }.await()
    }

    override suspend fun removeUserTutor(tutorId: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val userDocRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            // Проверяем, есть ли уже такой tutorId в массиве
            if (snapshot.exists()) {
                val tutors = snapshot.get("tutors") as? List<String> ?: listOf()
                if (!tutors.contains(tutorId)) {
                    transaction.update(userDocRef, "tutors", FieldValue.arrayRemove(tutorId))
                }
            }
            // Возвращаем результат для дальнейшего использования, если необходимо
            snapshot
        }.await()
    }

    override suspend fun getUserSubjectsWithPrices(): List<SubjectWithPrice>? =
        withContext(Dispatchers.IO) {
            val subjectsWithPrices = getUserDto().subjects ?: return@withContext null

            val subjectsTasks = subjectsWithPrices.map { subjectWithPrice ->
                async {
                    db.collection("subjects").document(subjectWithPrice.subjectId).get().await()
                        .toObject<SubjectDto>()
                        ?.let { subjectWithPrice.toDomain(it.toDomain()) }
                }
            }

            subjectsTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
        }

    override suspend fun getUserLanguagesWithLevels(): List<Language>? =
        withContext(Dispatchers.IO) {
            val languagesWithLevels = getUserDto().languages ?: return@withContext null

            val languagesTasks = languagesWithLevels.map { languageWithLevel ->
                async {
                    db.collection("languages").document(languageWithLevel.languageId).get()
                        .await()
                        .toObject<LanguageDto>()
                        ?.let { languageWithLevel.toDomain(it.name) }
                }
            }

            languagesTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
        }

    override suspend fun getUserEducations(): List<Education>? = withContext(Dispatchers.IO) {
        val educations = getUserDto().educations ?: return@withContext null

        val educationsTasks = educations.map { education ->
            async {
                db.collection("education_types").document(education.typeId).get()
                    .await()
                    .toObject<EducationTypeDto>()
                    ?.let { education.toDomain(it.toDomain()) }
            }
        }

        educationsTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
    }

    override suspend fun getStudents(): List<UserDto> {
        val querySnapshot = db.collection("users").whereEqualTo("canBeTutor", false).get().await()
        if (!querySnapshot.isEmpty) {
            val students = mutableListOf<UserDto>()
            querySnapshot.documents.forEach { document ->
                val user = document.toObject<UserDto>()
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

    override suspend fun getEducationTypes(): List<EducationType> {
        if (educationTypes == null) {
            val querySnapshot = db.collection("education_types").get().await()
            educationTypes = querySnapshot.documents.map { document ->
                val educationType = document.toObject<EducationTypeDto>()
                educationType?.toDomain() ?: throw (IOException("Education type not found"))
            }
        }
        return educationTypes!!
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
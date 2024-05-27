package com.nailorsh.repeton.common.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice
import com.nailorsh.repeton.common.data.models.user.Student
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.data.models.user.User
import com.nailorsh.repeton.common.firestore.mappers.toDomain
import com.nailorsh.repeton.common.firestore.mappers.toDomainStudent
import com.nailorsh.repeton.common.firestore.mappers.toDomainTutor
import com.nailorsh.repeton.common.firestore.mappers.toDto
import com.nailorsh.repeton.common.firestore.models.EducationTypeDto
import com.nailorsh.repeton.common.firestore.models.HomeworkDto
import com.nailorsh.repeton.common.firestore.models.LanguageDto
import com.nailorsh.repeton.common.firestore.models.LanguageLevelDto
import com.nailorsh.repeton.common.firestore.models.LanguageWithLevelDto
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
    private var languages: List<Language>? = null
    private var languageLevels: List<LanguageLevel>? = null

    override suspend fun sendHomeworkMessage(lessonId: Id, message: String) {
        db.collection("lessons").document(lessonId.value).collection("homework").document()
            .collection("reviews").document().set(
                ReviewDto(text = message)
            )
    }

    override suspend fun updateCurrentUserName(name: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("name", name).await()
    }

    override suspend fun updateCurrentUserSurname(surname: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("surname", surname).await()
    }

    override suspend fun updateCurrentUserPhotoSrc(url: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("photoSrc", url).await()
    }

    override suspend fun updateCurrentUserAbout(about: String) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        db.collection("users").document(userId).update("about", about).await()
    }

    override suspend fun getCurrentUserDto(): UserDto {
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

    private suspend fun getUserDto(userId: Id): UserDto = withContext(Dispatchers.IO) {
        db.collection("users")
            .document(userId.value)
            .get()
            .await()
            .toObject<UserDto>()
            ?: throw NoSuchElementException("User not found for id: ${userId.value}")
    }

    override suspend fun getCurrentUser(): User {
        return getUser(getCurrentUserId())
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

    override suspend fun getUser(userId: Id): User {
        return getUserDto(userId).toDomain()
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

    override suspend fun getCurrentUserId(): Id {
        return Id(getCurrentUserDto().id)
    }

    override suspend fun getCurrentUserType(): Boolean {
        return getCurrentUserDto().canBeTutor
    }

    override suspend fun getUserType(userId: Id): Boolean {
        return if (userId == getCurrentUserId()) getCurrentUserDto().canBeTutor
        else getUserDto(userId).canBeTutor
    }

    override suspend fun getCurrentUserName(): String {
        return getCurrentUserDto().name
    }

    override suspend fun getUserName(userId: Id): String {
        return if (userId == getCurrentUserId()) getCurrentUserDto().name
        else getUserDto(userId).name
    }

    override suspend fun getCurrentUserSurname(): String {
        return getCurrentUserDto().surname
    }

    override suspend fun getUserSurname(userId: Id): String {
        return if (userId == getCurrentUserId()) getCurrentUserDto().surname
        else getUserDto(userId).surname
    }

    override suspend fun getCurrentUserPhotoSrc(): String? {
        return getCurrentUserDto().photoSrc
    }

    override suspend fun getUserPhotoSrc(userId: Id): String? {
        return if (userId == getCurrentUserId()) getCurrentUserDto().photoSrc
        else getUserDto(userId).photoSrc
    }

    override suspend fun getCurrentUserAbout(): String? {
        return getCurrentUserDto().about
    }

    override suspend fun getUserAbout(userId: Id): String? {
        return if (userId == getCurrentUserId()) getCurrentUserDto().about
        else getUserDto(userId).about
    }

    override suspend fun getCurrentUserStudents(): List<Student>? = withContext(Dispatchers.IO) {
        val studentsIds = getCurrentUserDto().students ?: return@withContext null

        val studentTasks = studentsIds.map { studentId ->
            async {
                db.collection("users").document(studentId).get().await()
                    .toObject<UserDto>()
                    ?.toDomainStudent()
            }
        }

        studentTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
    }

    override suspend fun addCurrentUserStudent(studentId: String) {
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

    override suspend fun removeCurrentUserStudent(studentId: String) {
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

    override suspend fun getCurrentUserTutors(): List<Tutor>? = withContext(Dispatchers.IO) {
        val tutorsIds = getCurrentUserDto().tutors ?: return@withContext null

        val tutorTasks = tutorsIds.map { tutorId ->
            async {
                db.collection("users").document(tutorId).get()
                    .await()
                    .toObject<UserDto>()
                    ?.toDomainTutor(
                        subjectsPrices = getUserSubjectsWithPrices(Id(tutorId)),
                        languagesWithLevels = getUserLanguagesWithLevels(Id(tutorId)),
                        educations = getUserEducations(Id(tutorId))
                    )
            }
        }

        tutorTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
    }

    override suspend fun addCurrentUserTutor(tutorId: String) {
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

    override suspend fun removeCurrentUserTutor(tutorId: Id) {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val userDocRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            // Проверяем, есть ли уже такой tutorId в массиве
            if (snapshot.exists()) {
                val tutors = snapshot.get("tutors") as? List<String> ?: listOf()
                if (!tutors.contains(tutorId.value)) {
                    transaction.update(userDocRef, "tutors", FieldValue.arrayRemove(tutorId.value))
                }
            }
            // Возвращаем результат для дальнейшего использования, если необходимо
            snapshot
        }.await()
    }

    override suspend fun getUserSubjectsWithPrices(userId: Id): List<SubjectWithPrice>? =
        withContext(Dispatchers.IO) {
            val subjectsWithPrices = getUserDto(userId).subjects ?: return@withContext null

            val subjectsTasks = subjectsWithPrices.map { subjectWithPrice ->
                async {
                    db.collection("subjects").document(subjectWithPrice.subjectId).get().await()
                        .toObject<SubjectDto>()
                        ?.let { subjectWithPrice.toDomain(it.toDomain()) }
                }
            }

            subjectsTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
        }

    override suspend fun getCurrentUserSubjectsWithPrices(): List<SubjectWithPrice>? =
        withContext(Dispatchers.IO) {
            getUserSubjectsWithPrices(getCurrentUserId())
        }

    override suspend fun updateCurrentUserSubjectsWithPrices(userSubjects: List<SubjectWithPrice>?) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users").document(getCurrentUserId().value)

            db.runTransaction { transaction ->

                val updatedSubjects = userSubjects?.map { it.toDto() }

                transaction.update(userRef, "subjects", updatedSubjects)
            }.await()
        }
    }

    override suspend fun updateCurrentUserLanguageLevel(languageId: Id, level: LanguageLevel) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users").document(getCurrentUserId().value)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val languages =
                    snapshot.get("languages") as? List<LanguageWithLevelDto> ?: emptyList()

                val updatedLanguages = languages.toMutableList()

                val index = languages.indexOfFirst { it.languageId == languageId.value }
                if (index != -1) {
                    updatedLanguages[index] = updatedLanguages[index].copy(level = level.value)
                } else {
                    updatedLanguages.add(LanguageWithLevelDto(languageId.value, level.value))
                }

                transaction.update(userRef, "languages", updatedLanguages)
            }.await()
        }
    }

    override suspend fun getUserLanguagesWithLevels(userId: Id): List<LanguageWithLevel>? =
        withContext(Dispatchers.IO) {
            val languagesWithLevels = getUserDto(userId).languages ?: return@withContext null
            val languagesTasks = languagesWithLevels.map { languageWithLevel ->
                async {
                    db.collection("languages").document(languageWithLevel.languageId).get()
                        .await()
                        .toObject<LanguageDto>()
                        .apply { Log.d("TutorLanguage", "$this") }
                        ?.let {
                            languageWithLevel.toDomain(
                                language = getLanguage(Id(it.id))
                            )
                        }
                }
            }

            languagesTasks.awaitAll().filterNotNull().takeIf { it.isNotEmpty() }
        }

    override suspend fun getCurrentUserLanguagesWithLevels(): List<LanguageWithLevel>? =
        withContext(Dispatchers.IO) {
            getUserLanguagesWithLevels(getCurrentUserId())
        }

    override suspend fun updateCurrentUserLanguagesWithLevels(languagesWithLevels: List<LanguageWithLevel>) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users").document(getCurrentUserId().value)
            db.runTransaction { transaction ->
                val updatedLanguages = languagesWithLevels.map { it.toDto() }
                transaction.update(userRef, "languages", updatedLanguages)
            }
        }
    }

    override suspend fun getUserEducations(userId: Id): List<Education>? =
        withContext(Dispatchers.IO) {
            val educations = getUserDto(userId).educations ?: return@withContext null

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

    override suspend fun getCurrentUserEducations(): List<Education>? =
        withContext(Dispatchers.IO) {
            getUserEducations(getCurrentUserId())
        }

    override suspend fun updateCurrentUserEducations(educations: List<Education>) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users")
                .document(getCurrentUserId().value)
                .collection("educations")

            // Получаем текущие документы в подколлекции "educations"
            val currentEducations = userRef.get().await()

            // Удаляем все существующие документы в подколлекции "educations"
            currentEducations.documents.forEach { document ->
                userRef.document(document.id).delete().await()
            }

            // Добавляем новые образования
            educations.forEach { education ->
                val educationRef = userRef.document()
                val educationDto = education.toDto()
                educationRef.set(educationDto).await()
            }
        }
    }

    override suspend fun getStudents(): List<Student> = withContext(Dispatchers.IO) {
        db.collection("users")
            .whereEqualTo("canBeTutor", false)
            .get()
            .await()
            .toObjects<UserDto>()
            .map { it.toDomainStudent() }
    }

    override suspend fun getTutors(): List<Tutor> = withContext(Dispatchers.IO) {
        db.collection("users")
            .whereEqualTo("canBeTutor", true)
            .get()
            .await()
            .toObjects<UserDto>()
            .map {
                it.toDomainTutor(
                    subjectsPrices = getUserSubjectsWithPrices(Id(it.id)),
                    languagesWithLevels = getUserLanguagesWithLevels(Id(it.id)),
                    educations = getUserEducations(Id(it.id))
                )
            }
    }

    override suspend fun getTutor(id: Id): Tutor = withContext(Dispatchers.IO) {
        db.collection("users")
            .document(id.value)
            .get()
            .await()
            .toObject<UserDto>()
            ?.toDomainTutor(
                subjectsPrices = getUserSubjectsWithPrices(id),
                languagesWithLevels = getUserLanguagesWithLevels(id),
                educations = getUserEducations(id)
            ) ?: throw NoSuchElementException("Tutor not found for id: ${id.value}")
    }
    private suspend fun addLessonIdToUser(userId: String, lessonId: String) {
        val userRef = db.collection("users").document(userId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val lessonIds = snapshot.get("lessons") as? List<String> ?: emptyList()
            val updatedLessonIds = lessonIds + lessonId
            transaction.update(userRef, "lessons", updatedLessonIds)
        }.await()
    }
    override suspend fun addLesson(newLesson: Lesson) = withContext(Dispatchers.IO) {
        // Convert Lesson to LessonDto
        val lessonDto = newLesson.toDto()

        // Add lesson to "lessons" collection
        val lessonRef = db.collection("lessons").document()
//        lessonDto.id = lessonRef.id // Set the generated ID to lessonDto
        val lessonId = lessonRef.id
        lessonRef.set(lessonDto).await()

        // Add homework if it exists
        newLesson.homework?.let { homework ->
            val homeworkRef = lessonRef.collection("homework").document()
//            homeworkDto.id = homeworkRef.id // Set the generated ID to homeworkDto
            homeworkRef.set(homework).await()

            homework.attachments?.forEach { attachment ->
                val attachmentRef = homeworkRef.collection("attachments").document()
//                attachmentDto.id = attachmentRef.id // Set the generated ID to attachmentDto
                attachmentRef.set(attachment).await()
            }
        }

        addLessonIdToUser(newLesson.tutor.id.value, lessonId)

        // Добавляем lessonId всем ученикам
        newLesson.studentIds.forEach { studentId ->
            addLessonIdToUser(studentId.value, lessonId)
        }
    }


    override suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
        val userLessons = getUserDto(getCurrentUserId()).lessons ?: return@withContext emptyList<Lesson>()
        if (userLessons.isEmpty()) {
            return@withContext emptyList<Lesson>()
        }

        // Запрашиваем документы уроков, идентификаторы которых содержатся в списке userLessons
        val lessonsDto = db.collection("lessons")
            .whereIn(FieldPath.documentId(), userLessons)
            .get()
            .await()
            .toObjects<LessonDto>()

        lessonsDto.map {
            val tutor = getTutor(Id(it.tutorId))
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
                tutor = getTutor(Id(lesson.tutorId))
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

    override suspend fun addCurrentUserLanguageWithLevel(languageWithLevel: LanguageWithLevel) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users").document(getCurrentUserId().value)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val languages =
                    snapshot.get("languages") as? List<LanguageWithLevelDto> ?: emptyList()

                val updatedLanguages = languages + languageWithLevel.toDto()
                transaction.update(userRef, "languages", updatedLanguages)
            }
        }
    }


    override suspend fun removeCurrentUserLanguageWithLevel(languageId: Id) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users").document(getCurrentUserId().value)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val languages =
                    snapshot.get("languages") as? List<LanguageWithLevelDto> ?: emptyList()

                val updatedLanguages = languages.toMutableList()

                // Находим индекс языка с указанным languageId и удаляем его из списка
                val index = languages.indexOfFirst { it.languageId == languageId.value }
                if (index != -1) {
                    updatedLanguages.removeAt(index)
                }

                transaction.update(userRef, "languages", updatedLanguages)
            }.await()
        }
    }

    override suspend fun addCurrentUserEducation(education: Education) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users")
                .document(getCurrentUserId().value)
                .collection("educations")

            val newEducationRef = userRef.document()
            val educationDto = education.copy(id = Id(newEducationRef.id)).toDto()

            newEducationRef.set(educationDto).await()
        }
    }

    override suspend fun updateCurrentUserEducation(education: Education) {
        withContext(Dispatchers.IO) {
            val educationDto = education.toDto()
            val userRef = db.collection("users")
                .document(getCurrentUserId().value)
                .collection("educations")
                .document(educationDto.id)

            userRef.set(educationDto).await()
        }
    }

    override suspend fun removeCurrentUserEducation(educationId: Id) {
        withContext(Dispatchers.IO) {
            val userRef = db.collection("users")
                .document(getCurrentUserId().value)
                .collection("educations")
                .document(educationId.value)

            userRef.delete().await()
        }
    }

    override suspend fun getLanguages(): List<Language> {
        if (languages == null) {
            val querySnapshot = db.collection("languages").get().await()
            languages = querySnapshot.documents.map { document ->
                val language = document.toObject<LanguageDto>()
                language?.toDomain() ?: throw (IOException("Language not found"))
            }
        }
        return languages!!
    }

    override suspend fun getLanguage(id: Id): Language {
        val document = db.collection("languages").document(id.value).get().await()

        if (document.exists()) {
            val language = document.toObject<LanguageDto>()

            return language?.toDomain() ?: throw (IOException("Error parsing language"))
        } else throw (IOException("Language not found"))
    }
}
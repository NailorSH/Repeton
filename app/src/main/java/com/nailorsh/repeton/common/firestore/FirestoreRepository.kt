package com.nailorsh.repeton.common.firestore

import com.google.firebase.auth.FirebaseUser
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.contact.Contact
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
import com.nailorsh.repeton.common.firestore.models.UserDto

interface FirestoreRepository {
    suspend fun getSignedInUser(): FirebaseUser?
    suspend fun signoutUser()
    suspend fun getCurrentUserId(): Id
    suspend fun getCurrentUserDto(): UserDto
    suspend fun getCurrentUser(): User
    suspend fun getUser(userId: Id): User

    // UserType
    suspend fun getUserType(userId: Id): Boolean
    suspend fun getCurrentUserType(): Boolean

    // UserName
    suspend fun getUserName(userId: Id): String
    suspend fun getCurrentUserName(): String
    suspend fun updateCurrentUserName(name: String)

    // UserSurname
    suspend fun getUserSurname(userId: Id): String
    suspend fun getCurrentUserSurname(): String
    suspend fun updateCurrentUserSurname(surname: String)

    // UserPhotoSrc
    suspend fun getUserPhotoSrc(userId: Id): String?
    suspend fun getCurrentUserPhotoSrc(): String?
    suspend fun updateCurrentUserPhotoSrc(url: String)

    // UserAbout
    suspend fun getUserAbout(userId: Id): String?
    suspend fun getCurrentUserAbout(): String?
    suspend fun updateCurrentUserAbout(about: String)

    // UserStudents
    suspend fun getCurrentUserStudents(): List<Student>?
    suspend fun addCurrentUserStudent(studentId: String)
    suspend fun removeCurrentUserStudent(studentId: String)

    // UserTutors
    suspend fun getCurrentUserTutors(): List<Tutor>?
    suspend fun addCurrentUserTutor(tutorId: String)
    suspend fun removeCurrentUserTutor(tutorId: Id)

    // UserSubjectsWithPrices
    suspend fun getUserSubjectsWithPrices(userId: Id): List<SubjectWithPrice>?
    suspend fun getCurrentUserSubjectsWithPrices(): List<SubjectWithPrice>?
    suspend fun updateCurrentUserSubjectsWithPrices(userSubjects : List<SubjectWithPrice>?)

    // UserLanguagesWithLevels
    suspend fun getUserLanguagesWithLevels(userId: Id): List<LanguageWithLevel>?
    suspend fun getCurrentUserLanguagesWithLevels(): List<LanguageWithLevel>?
    suspend fun updateCurrentUserLanguagesWithLevels(languagesWithLevels: List<LanguageWithLevel>)
    suspend fun addCurrentUserLanguageWithLevel(languageWithLevel: LanguageWithLevel)
    suspend fun updateCurrentUserLanguageLevel(languageId: Id, level: LanguageLevel)
    suspend fun removeCurrentUserLanguageWithLevel(languageId: Id)

    // UserEducations
    suspend fun getUserEducations(userId: Id): List<Education>?
    suspend fun getCurrentUserEducations(): List<Education>?
    suspend fun updateCurrentUserEducations(educations: List<Education>)
    suspend fun addCurrentUserEducation(education: Education)
    suspend fun updateCurrentUserEducation(education: Education)
    suspend fun removeCurrentUserEducation(educationId: Id)

    // UserContacts
    suspend fun getUserContacts(userId: Id): List<Contact>?
    suspend fun getCurrentUserContacts(): List<Contact>?
    suspend fun updateCurrentUserContacts(contacts: List<Contact>)
    suspend fun addCurrentUserContact(contact: Contact)
    suspend fun updateCurrentUserContact(contact: Contact)
    suspend fun removeCurrentUserContact(contactId: Id)

    // EducationTypes
    suspend fun getEducationTypes(): List<EducationType>

    // Students
    suspend fun getStudents(): List<Student>

    // Tutors
    suspend fun getTutors(): List<Tutor>
    suspend fun getTutor(id: Id): Tutor?

    // Lesson
    suspend fun getLessons(): List<Lesson>
    suspend fun addLesson(newLesson: Lesson)
    suspend fun getLesson(id: Id): Lesson


    // Subject
    suspend fun getSubjects(): List<Subject>
    suspend fun getSubject(id: Id): Subject

    // Homework
    suspend fun getHomework(lessonId: Id): Homework
    suspend fun sendHomeworkMessage(lessonId: Id, message: String)

    // Language
    suspend fun getLanguages(): List<Language>
    suspend fun getLanguage(id: Id): Language

    // LanguageLevels
    suspend fun getLanguageLevels(): List<LanguageLevel>?
}
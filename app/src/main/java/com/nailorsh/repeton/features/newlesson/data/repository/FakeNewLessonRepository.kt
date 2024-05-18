package com.nailorsh.repeton.features.newlesson.data.repository


//class FakeNewLessonRepository @Inject constructor() : NewLessonRepository {
//    override suspend fun getSubjects(filter: String): List<String> = withContext(Dispatchers.IO) {
//        FakeSubjectsSource.getSubjects().filter { subject ->
//            subject.name["ru"]!!.lowercase().startsWith(filter.lowercase())
//        }.map { subject -> subject.name["ru"]!!.toString() }
//    }
//
//    override suspend fun saveNewLesson(lesson: Lesson) = withContext(Dispatchers.IO) {
//        FakeLessonSource.addLesson(lesson)
//    }
//
//    override suspend fun getSubject(subjectName: String) = withContext(Dispatchers.IO) {
//        FakeSubjectsSource.getSubjectByName(subjectName)
//    }
//
//    override suspend fun getStudents(): List<NewLessonUserItem> {
//        return emptyList()
////        return FakeTutorsSource.getTutorsList()
//    }
//
//}
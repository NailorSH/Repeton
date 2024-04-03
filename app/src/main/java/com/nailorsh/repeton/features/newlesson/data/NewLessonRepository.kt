package com.nailorsh.repeton.features.newlesson.data

interface NewLessonRepository {

    /* TODO Заменить String на Subject */
    suspend fun getSubjects() : List<String>
}
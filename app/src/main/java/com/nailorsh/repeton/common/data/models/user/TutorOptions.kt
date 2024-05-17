package com.nailorsh.repeton.common.data.models.user

sealed interface TutorOptions {

    data class Lessons(val lessons: List<Lessons>?) : TutorOptions

    data class Students(val students: List<User>) : TutorOptions

    /* TODO Реализация статистики */
    data class Statistics(val statistics: List<String>) : TutorOptions

}
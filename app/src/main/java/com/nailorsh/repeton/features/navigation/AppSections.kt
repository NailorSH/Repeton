package com.nailorsh.repeton.features.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nailorsh.repeton.R

enum class AppSections(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    SEARCH(R.string.search_tutors, R.drawable.search_icon, "search"),
    HOME(R.string.home, R.drawable.home_icon, "home"),
    CHATS(R.string.chats, R.drawable.chat_icon, "chats"),
    PROFILE(R.string.profile, R.drawable.profile_icon, "profile"),
    LESSON(R.string.lesson, R.drawable.home_icon, "lesson/{id}"),
    TUTOR(R.string.tutor, R.drawable.search_icon, "tutor/{id}"),
    NEW_LESSON(R.string.new_lesson_route_title, R.drawable.home_icon, "new_lesson"),
    NEW_LESSON_SECOND(R.string.new_lesson_second_route_title, R.drawable.home_icon, "new_lesson_2")
}
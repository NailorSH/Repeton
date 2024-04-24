package com.nailorsh.repeton.features.userprofile.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nailorsh.repeton.R
import java.lang.StackWalker.Option
import javax.inject.Inject


enum class OptionType() {
    Tutor, Student, Setting
}

enum class Options(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val type: OptionType,
    val trailingItem : TrailingContent
) {
    Lessons(
        icon = R.drawable.ic_profilescreen_lessons,
        text = R.string.profile_screen_lessons,
        type = OptionType.Tutor,
        trailingItem = TrailingContent.Empty
    ),

    Students(
        icon = R.drawable.ic_profilescreen_students,
        text = R.string.profile_screen_students,
        type = OptionType.Tutor,
        trailingItem = TrailingContent.Empty
    ),

    Homework(
        icon = R.drawable.ic_profilescreen_homework,
        text = R.string.profile_screen_homework,
        type = OptionType.Tutor,
        trailingItem = HomeworkBadge(0)
    ),
    Statistics(
        icon = R.drawable.ic_profilescreen_statistics,
        text = R.string.profile_screen_statistics,
        type = OptionType.Tutor,
        trailingItem = TrailingContent.Empty
    ),
    About(
        icon = R.drawable.ic_profilescreen_about,
        text = R.string.profile_screen_about,
        type = OptionType.Tutor,
        trailingItem = TrailingContent.Empty
    ),

    Security(
        icon = R.drawable.ic_profilescreen_security,
        text = R.string.profile_screen_security,
        type = OptionType.Setting,
        trailingItem = TrailingContent.Empty
    ),

    Notifications(
        icon = R.drawable.ic_profilescreen_notifications,
        text = R.string.profile_screen_notification,
        type = OptionType.Setting,
        trailingItem = TrailingContent.Empty
    ),

    Language(
        icon = R.drawable.ic_profilescreen_language,
        text = R.string.profile_screen_language,
        type = OptionType.Setting,
        trailingItem = TrailingContent.Empty
    ),

    Help(
        icon = R.drawable.ic_profilescreen_help,
        text = R.string.profile_screen_help,
        type = OptionType.Setting,
        trailingItem = TrailingContent.Empty
    ),

    ThemeSwitch(
        icon = R.drawable.ic_profilescreen_theme,
        text = R.string.profile_screen_theme,
        type = OptionType.Setting,
        trailingItem = ThemeSwitcher(false)
    )
}

class ActualProfileRepository @Inject constructor() : UserProfileRepository  {

    override suspend fun getSettingsOptions(): List<Options> =
        Options.values().filter { it.type == OptionType.Setting }


    override suspend fun getTutorOptions(): List<Options> =
        Options.values().filter { it.type == OptionType.Tutor }


    override suspend fun getStudentOptions(): List<Options> =
        Options.values().filter { it.type == OptionType.Student }
}
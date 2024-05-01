package com.nailorsh.repeton.features.navigation.routes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.navigation.NavigationRoute

sealed class BottomBarScreen(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    override val route: String,
) : NavigationRoute {
    object Search : BottomBarScreen(
        title = R.string.search_tutors,
        icon = R.drawable.ic_search_icon,
        route = "search"
    )

    object Home : BottomBarScreen(
        title = R.string.home,
        icon = R.drawable.ic_home_icon,
        route = "home"
    )

    object Chats : BottomBarScreen(
        title = R.string.chats,
        icon = R.drawable.ic_chat_icon,
        route = "chats"
    )

    object Profile : BottomBarScreen(
        title = R.string.profile,
        icon = R.drawable.ic_profile_icon,
        route = "profile"
    )
}
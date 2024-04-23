package com.nailorsh.repeton.features.userprofile.presentation.viewmodel

import android.graphics.Path.Op
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.schedule.data.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class TrailingItems {
    None, Switch, Badge
}


interface TrailingContent {

    @Composable
    fun Content()

    companion object {
        val Empty = object : TrailingContent {
            @Composable
            override fun Content() = Unit
        }
    }
}

class HomeworkBadge(count: Int) : TrailingContent {
    private var _count by mutableStateOf(count)

    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }


}


class ThemeSwitch(switchState: Boolean) : TrailingContent {
    private var _state by mutableStateOf(switchState)

    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }


}

enum class UserType() {
    Tutor, Student, None
}

enum class Options(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val type: UserType,
) {
    Lessons(
        icon = R.drawable.ic_profilescreen_lessons,
        text = R.string.profile_screen_lessons,
        type = UserType.Tutor
    )
}


data class ProfileScreenOption(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val onPressCallback: () -> Unit,
    val trailingItem: TrailingItems,
    val onSwitchChange: (() -> Unit)? = {},
    val switchState: Boolean? = false,
    val badgeCount: Int? = 0
)

data class TrailingItemsState(
    val badgeCount: Int = 0,
    val switchState: Boolean = false
)

sealed interface ProfileScreenUiState {

    data class Success(
        val profileOptions: List<ProfileScreenOption>,
        val settingsOptions: List<ProfileScreenOption>
    ) : ProfileScreenUiState

    object Loading : ProfileScreenUiState
    object Error : ProfileScreenUiState

}


@HiltViewModel
class ScheduleViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    private val _trailingItemsState = MutableStateFlow(TrailingItemsState())

    private val _sideEffect = MutableSharedFlow<BottomBarScreen>()
    val sideEffect: SharedFlow<BottomBarScreen> = _sideEffect.asSharedFlow()

    init {
        getOptions()
    }


    fun getOptions() {
        viewModelScope.launch {
            val profileOptions = listOf(
                ProfileScreenOption(
                    icon = R.drawable.ic_profilescreen_lessons,
                    text = R.string.profile_screen_lessons,
                    onPressCallback = {},
                    trailingItem = TrailingItems.None
                ),
                ProfileScreenOption(
                    icon = R.drawable.ic_profilescreen_students,
                    text = R.string.profile_screen_students,
                    onPressCallback = {},
                    trailingItem = TrailingItems.None
                ),
                ProfileScreenOption(
                    icon = R.drawable.ic_profilescreen_homework,
                    text = R.string.profile_screen_homework,
                    onPressCallback = {},
                    trailingItem = TrailingItems.Badge
                ),
                ProfileScreenOption(
                    icon = R.drawable.ic_profilescreen_statistics,
                    text = R.string.profile_screen_statistics,
                    onPressCallback = {},
                    trailingItem = TrailingItems.None
                ),
                ProfileScreenOption(
                    icon = R.drawable.ic_profilescreen_about,
                    text = R.string.profile_screen_about,
                    onPressCallback = {},
                    trailingItem = TrailingItems.None
                )
            )

            val settingsOptions = listOf(

            )
        }


        fun onOptionClicked(option: Options) {
            viewModelScope.launch {
                when (option) {
                    Options.Lessons -> _sideEffect.emit(BottomBarScreen.Home)
                }
            }
        }
    }

}
package com.nailorsh.repeton.features.userprofile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.navigation.NavigationRoute
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.navigation.routes.ProfileScreen
import com.nailorsh.repeton.features.settings.UserSettingsRepository
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.data.TrailingContentType
import com.nailorsh.repeton.features.userprofile.data.UserProfileRepository
import com.nailorsh.repeton.features.userprofile.data.models.ProfileUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface ProfileScreenUiState {

    data class Success(
        val state: ProfileScreenState
    ) : ProfileScreenUiState

    object Loading : ProfileScreenUiState
    object Error : ProfileScreenUiState

}

data class ProfileScreenState(
    val profileUserData: ProfileUserData,
    val profileOptions: List<Options>,
    val settingsOptions: List<Options>,
)


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: UserProfileRepository,
    private val settingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileScreenUiState>(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NavigationRoute>()
    val sideEffect: SharedFlow<NavigationRoute> = _sideEffect.asSharedFlow()


    suspend fun getOptions() = withContext(Dispatchers.IO) {
        _uiState.update { ProfileScreenUiState.Loading }
        try {
            val profileOptions = profileRepository.getUserOptions()
            val settingsOptions = profileRepository.getSettingsOptions()
            val profileUserData = profileRepository.getUserData()
            _uiState.update {
                ProfileScreenUiState.Success(
                    ProfileScreenState(
                        profileUserData = profileUserData,
                        profileOptions = profileOptions,
                        settingsOptions = settingsOptions
                    )

                )
            }
        } catch (e: IOException) {
            _uiState.update { ProfileScreenUiState.Error }
        } catch (e: HttpRetryException) {
            _uiState.update { ProfileScreenUiState.Error }
        }
    }


    init {
        viewModelScope.launch {
            getOptions()
            subscribeToTheme()
        }
    }

    private suspend fun subscribeToTheme() = withContext(Dispatchers.IO) {
        settingsRepository.getTheme().collect { isDark ->
            _uiState.update { state ->
                when (state) {
                    ProfileScreenUiState.Error -> state
                    ProfileScreenUiState.Loading -> state
                    is ProfileScreenUiState.Success -> state.copy(
                        state = state.state.copy(
                            settingsOptions = state.state.settingsOptions.map { it ->
                                if (it is Options.ThemeSwitch) {
                                    it.copy(
                                        icon = if (!isDark) R.drawable.ic_dark_theme else R.drawable.ic_light_theme,
                                        trailingItem = TrailingContentType.ThemeSwitcher(
                                            isEnabled = isDark,
                                            onSwitchCallback = { isDarkTheme ->
                                                onThemeUpdate(
                                                    isDarkTheme
                                                )
                                            }
                                        )
                                    )
                                } else {
                                    it
                                }
                            }
                        )
                    )
                }
            }
        }
    }

    fun onThemeUpdate(isDarkThemeEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateTheme(isDarkThemeEnabled)
        }
    }

    fun onOptionClicked(option: Options) { // Передаётся параметром в NavGraph'e
        viewModelScope.launch {
            when (option) {
                is Options.Subjects -> _sideEffect.emit(ProfileScreen.SUBJECTS)
                is Options.LessonsTutor -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Students -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Statistics -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.AboutTutor -> _sideEffect.emit(ProfileScreen.ABOUT)
                is Options.Security -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Notifications -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Language -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Help -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Homework -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.ThemeSwitch -> {}
                is Options.AboutStudent -> _sideEffect.emit(ProfileScreen.ABOUT)
                is Options.LessonsStudent -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Tutors -> _sideEffect.emit(BottomBarScreen.Home)
            }
        }
    }


}
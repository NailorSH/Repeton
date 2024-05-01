package com.nailorsh.repeton.features.userprofile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.core.navigation.NavigationRoute
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.settings.UserSettingsRepository
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.data.TrailingContentType
import com.nailorsh.repeton.features.userprofile.data.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface ProfileScreenUiState {

    data class Success(
        val profileOptions: List<Options>,
        val settingsOptions: List<Options>,
    ) : ProfileScreenUiState

    object Loading : ProfileScreenUiState
    object Error : ProfileScreenUiState

}


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: UserProfileRepository,
    private val settingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileScreenUiState>(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NavigationRoute>()
    val sideEffect: SharedFlow<NavigationRoute> = _sideEffect.asSharedFlow()


    fun getOptions() {
        viewModelScope.launch {
            _uiState.update { ProfileScreenUiState.Loading }
            try {

                val profileOptions = profileRepository.getTutorOptions()
                val settingsOptions = profileRepository.getSettingsOptions()
                _uiState.update {
                    ProfileScreenUiState.Success(
                        profileOptions, settingsOptions
                    )
                }
            } catch (e: IOException) {
                _uiState.update { ProfileScreenUiState.Error }
            } catch (e: HttpRetryException) {
                _uiState.update { ProfileScreenUiState.Error }
            }
        }
    }


    init {
        viewModelScope.launch {
            getOptions()
            subscribeToTheme()
        }
    }

    private suspend fun subscribeToTheme() {
        settingsRepository.getTheme().collect { theme ->
            _uiState.update { state ->
                when (val currentState = _uiState.value) {
                    ProfileScreenUiState.Error -> currentState
                    ProfileScreenUiState.Loading -> currentState
                    is ProfileScreenUiState.Success -> currentState.copy(
                        settingsOptions = currentState.settingsOptions.map {
                            if (it is Options.ThemeSwitch) {
                                it.copy(trailingItem = TrailingContentType.ThemeSwitcher(
                                    isEnabled = theme,
                                    onSwitchCallback = this::onThemeUpdate
                                ))
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        }
    }

    fun onThemeUpdate(theme : Boolean) {
        viewModelScope.launch {
            settingsRepository.updateTheme(theme)
        }
    }

    fun onOptionClicked(option: Options) { // Передаётся параметром в NavGraph'e
        viewModelScope.launch {
            when (option) {
                is Options.Lessons -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Students -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Statistics -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.About -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Security -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Notifications -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Language -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Help -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.Homework -> _sideEffect.emit(BottomBarScreen.Home)
                is Options.ThemeSwitch -> {
                    onThemeUpdate(false)
                }
            }
        }
    }


}
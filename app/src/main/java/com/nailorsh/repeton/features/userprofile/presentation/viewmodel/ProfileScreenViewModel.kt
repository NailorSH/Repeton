package com.nailorsh.repeton.features.userprofile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.core.navigation.NavigationRoute
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.data.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject


data class TrailingItemsState(
    val badgeCount: Int = 0,
    val switchState: Boolean = false
)

sealed interface ProfileScreenUiState {

    data class Success(
        val profileOptions: List<Options>,
        val settingsOptions: List<Options>
    ) : ProfileScreenUiState

    object Loading : ProfileScreenUiState
    object Error : ProfileScreenUiState

}


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileScreenUiState>(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    private val _trailingItemsState = MutableStateFlow(TrailingItemsState())

    private val _sideEffect = MutableSharedFlow<NavigationRoute>()
    val sideEffect: SharedFlow<NavigationRoute> = _sideEffect.asSharedFlow()

    init {
        getOptions()
    }


    fun getOptions() {
        viewModelScope.launch {
            _uiState.update { ProfileScreenUiState.Loading }
            try {
                val profileOptions = profileRepository.getTutorOptions()
                val settingsOptions = profileRepository.getSettingsOptions()
                _uiState.update {
                    ProfileScreenUiState.Success(profileOptions, settingsOptions)
                }
            } catch (e: IOException) {
                _uiState.update { ProfileScreenUiState.Error }
            } catch (e: HttpRetryException) {
                _uiState.update { ProfileScreenUiState.Error }
            }
        }
    }


    fun onOptionClicked(option: Options) { // Передаётся параметром в NavGraph'e
        viewModelScope.launch {
            when (option) {
                Options.Lessons -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Students -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Homework -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Statistics -> _sideEffect.emit(BottomBarScreen.Home)
                Options.About -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Security -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Notifications -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Language -> _sideEffect.emit(BottomBarScreen.Home)
                Options.Help -> _sideEffect.emit(BottomBarScreen.Home)
                Options.ThemeSwitch -> {}

            }
        }
    }


}
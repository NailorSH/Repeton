package com.nailorsh.repeton

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.core.settings.UserSettingsRepository
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface MainUiState {

    data class Success(
        val isDarkThemeEnabled: Boolean
    ) : MainUiState

    object Loading : MainUiState
    object Error : MainUiState

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            getTheme()
            userSettingsRepository.getTheme().collect { theme ->
                _uiState.update { state ->
                    when (val currentState = _uiState.value) {
                        is MainUiState.Error -> currentState
                        is MainUiState.Loading -> currentState
                        is MainUiState.Success -> currentState.copy(
                            isDarkThemeEnabled = theme
                        )
                    }
                }

            }
        }

    }

    private fun getTheme() {
        viewModelScope.launch {
            try {
                val isDarkThemeEnabled = userSettingsRepository.getTheme().first()
                _uiState.update {
                    MainUiState.Success(isDarkThemeEnabled)
                }
            } catch (e: IOException) {
                _uiState.update { MainUiState.Error }
            } catch (e: HttpRetryException) {
                _uiState.update { MainUiState.Error }
            }
        }
    }

}
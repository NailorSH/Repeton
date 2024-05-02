package com.nailorsh.repeton.features.navigation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.features.settings.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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
            userSettingsRepository.getTheme().collect { isDark ->
                _uiState.update { state ->
                    when (state) {
                        is MainUiState.Error -> state
                        is MainUiState.Loading -> state
                        is MainUiState.Success -> state.copy(
                            isDarkThemeEnabled = isDark
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
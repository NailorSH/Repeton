package com.nailorsh.repeton.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nailorsh.repeton.RepetonApplication
import com.nailorsh.repeton.data.RepetonRepository
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.model.Tutor
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException

sealed interface SearchUiState {
    data class Success(val tutors: List<Tutor>) : SearchUiState
    object Error : SearchUiState
    object Loading : SearchUiState
    object None : SearchUiState
}

sealed interface ScheduleUiState {
    data class Success(val lessons: List<Lesson>) : ScheduleUiState
    object Error : ScheduleUiState
    object Loading : ScheduleUiState
}

class RepetonViewModel(private val repetonRepository: RepetonRepository) : ViewModel() {
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.None)
        private set

    var scheduleUiState: ScheduleUiState by mutableStateOf(ScheduleUiState.Loading)
        private set

    init {
        getLessons()
    }

    fun getLessons() {
        viewModelScope.launch {
            scheduleUiState = ScheduleUiState.Loading
            scheduleUiState = try {
                ScheduleUiState.Success(repetonRepository.getLessons())
            } catch (e: IOException) {
                ScheduleUiState.Error
            } catch (e: HttpRetryException) {
                ScheduleUiState.Error
            }
        }
    }

    fun getTutors() {
        viewModelScope.launch {
            searchUiState = SearchUiState.Loading
            searchUiState = try {
                SearchUiState.Success(repetonRepository.getTutors())
            } catch (e: IOException) {
                SearchUiState.Error
            } catch (e: HttpRetryException) {
                SearchUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RepetonApplication)
                val repetonRepository = application.container.repetonRepository
                RepetonViewModel(repetonRepository = repetonRepository)
            }
        }
    }
}
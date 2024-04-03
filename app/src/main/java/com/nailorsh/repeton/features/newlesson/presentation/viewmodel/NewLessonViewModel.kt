package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject


sealed interface NewLessonUiState {
    /* TODO Заменить String на Subject */
    data class Success(val subjects: List<String>) : NewLessonUiState
    object Error : NewLessonUiState
    object Loading : NewLessonUiState
}

@HiltViewModel
class NewLessonViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {
    var newLessonUiState: NewLessonUiState by mutableStateOf(NewLessonUiState.Loading)
        private set

    init {
        getSubjects()
    }

    fun getSubjects() {
        viewModelScope.launch {
            newLessonUiState = NewLessonUiState.Loading
            newLessonUiState = try {
                /* TODO Добавить репозиторий для NewLesson и метод получения всех доступных предметов */
                val subjects = newLessonRepository.getSubjects()
                NewLessonUiState.Success(subjects)
            } catch (e: IOException) {
                NewLessonUiState.Error
            } catch (e: HttpRetryException) {
                NewLessonUiState.Error
            }
        }
    }
}
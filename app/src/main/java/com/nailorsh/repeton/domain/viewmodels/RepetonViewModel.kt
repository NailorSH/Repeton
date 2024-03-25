package com.nailorsh.repeton.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.domain.repositories.RepetonRepository
import com.nailorsh.repeton.model.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject


sealed interface CurrentLessonUiState {
    data class Success(val lesson: Lesson) : CurrentLessonUiState
    object Error : CurrentLessonUiState
    object Loading : CurrentLessonUiState
}

@HiltViewModel
class RepetonViewModel @Inject constructor(
    private val repetonRepository: RepetonRepository
) : ViewModel() {
    var currentLessonUiState: CurrentLessonUiState by mutableStateOf(CurrentLessonUiState.Loading)
        private set

    fun getLesson(lessonId: Int) {
        viewModelScope.launch {
            currentLessonUiState = CurrentLessonUiState.Loading
            currentLessonUiState = try {
                val lesson = repetonRepository.getLesson(lessonId)
                CurrentLessonUiState.Success(lesson)
            } catch (e: IOException) {
                CurrentLessonUiState.Error
            } catch (e: HttpRetryException) {
                CurrentLessonUiState.Error
            }
        }
    }
}
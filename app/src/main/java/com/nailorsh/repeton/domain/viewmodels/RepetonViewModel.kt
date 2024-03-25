package com.nailorsh.repeton.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.domain.repositories.RepetonRepository
import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject


sealed interface CurrentLessonUiState {
    data class Success(val lesson: Lesson) : CurrentLessonUiState
    object Error : CurrentLessonUiState
    object Loading : CurrentLessonUiState
}

sealed interface ChatsUiState {
    data class Success(val chats: List<Chat>) : ChatsUiState
    object Error : ChatsUiState
    object Loading : ChatsUiState
}

@HiltViewModel
class RepetonViewModel @Inject constructor(
    private val repetonRepository: RepetonRepository
) : ViewModel() {

    var chatsUiState: ChatsUiState by mutableStateOf(ChatsUiState.Loading)
        private set

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

    fun getChats() {
        viewModelScope.launch {
            chatsUiState = ChatsUiState.Loading
            delay(2000)
            chatsUiState = try {
                ChatsUiState.Success(repetonRepository.getChats())
            } catch (e: IOException) {
                ChatsUiState.Error
            } catch (e: HttpRetryException) {
                ChatsUiState.Error
            }
        }
    }
}
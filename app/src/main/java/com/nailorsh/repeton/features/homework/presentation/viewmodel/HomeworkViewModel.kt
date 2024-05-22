package com.nailorsh.repeton.features.homework.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.features.homework.data.models.HomeworkItem
import com.nailorsh.repeton.features.homework.data.repository.HomeworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface HomeworkUiState {
    object Loading : HomeworkUiState

    data class Error(val errorMsg: String) : HomeworkUiState

    data class Success(val state: HomeworkState) : HomeworkUiState
}

sealed interface HomeworkAction {
    object NavigateBack : HomeworkAction
    data class UpdateAnswerText(val text: String) : HomeworkAction
    data class ShowImageDialogue(val image: Attachment.Image) : HomeworkAction
    object HideImageDialogue : HomeworkAction
    object SendMessage : HomeworkAction
}

sealed interface HomeworkNavigationEvent {
    object NavigateBack : HomeworkNavigationEvent

}

data class HomeworkState(
    val homework: HomeworkItem,
    val answerText: String = "",

    val showImageDialogue: Boolean = false,
)

@HiltViewModel
class HomeworkViewModel @Inject constructor(
    private val homeworkRepository: HomeworkRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeworkUiState>(HomeworkUiState.Loading)
    val state = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeworkNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private lateinit var lessonId: String

    fun getHomework(homeworkID: Id) {
        lessonId = homeworkID.value
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = HomeworkUiState.Loading
            try {
                val homework: HomeworkItem = homeworkRepository.getHomework(homeworkID)
                _state.value = HomeworkUiState.Success(HomeworkState(homework))
            } catch (e: IOException) {
                _state.value = HomeworkUiState.Error(e.toString())
            } catch (e: HttpRetryException) {
                _state.value = HomeworkUiState.Error(e.toString())
            } catch (e: Exception) {
                _state.value = HomeworkUiState.Error(e.toString())
            }
        }
    }

    fun onAction(action: HomeworkAction) {
        viewModelScope.launch {
            when (action) {
                HomeworkAction.NavigateBack -> _navigationEvent.emit(HomeworkNavigationEvent.NavigateBack)
                else -> {
                    when (val state = _state.value) {
                        is HomeworkUiState.Success -> {
                            when (action) {
                                is HomeworkAction.SendMessage -> _state.update {
                                    homeworkRepository.sendHomeworkMessage(
                                        lessonId = Id(lessonId),
                                        message = state.state.answerText
                                    )
                                    state.copy(
                                        state.state.copy(
                                            answerText = ""
                                        )
                                    )
                                }

                                is HomeworkAction.UpdateAnswerText -> _state.update {
                                    state.copy(
                                        state.state.copy(
                                            answerText = action.text
                                        )
                                    )
                                }

                                is HomeworkAction.ShowImageDialogue -> _state.update {
                                    state.copy(
                                        state.state.copy(
                                            showImageDialogue = true
                                        )
                                    )
                                }

                                HomeworkAction.HideImageDialogue -> _state.update {
                                    state.copy(
                                        state.state.copy(
                                            showImageDialogue = false
                                        )
                                    )
                                }

                                else -> {}
                            }
                        }

                        else -> {

                        }
                    }

                }
            }


        }
    }

}
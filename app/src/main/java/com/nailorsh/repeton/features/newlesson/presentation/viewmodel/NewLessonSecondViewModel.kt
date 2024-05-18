package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.features.auth.data.FirebaseAuthRepository
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonFirstScreenData
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonHomework
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonItem
import com.nailorsh.repeton.features.newlesson.data.repository.NewLessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface NewLessonSecondUIState {
    object Error : NewLessonSecondUIState
    object Loading : NewLessonSecondUIState

    data class Success(val state: NewLessonSecondState) : NewLessonSecondUIState
}

sealed interface NewLessonSecondNavigationEvent {
    object NavigateBack : NewLessonSecondNavigationEvent
    object SaveLesson : NewLessonSecondNavigationEvent
}


sealed class NewLessonSecondUIEvent(@StringRes val msg: Int) {

    data class CameraFail(@StringRes val errorMsg: Int) : NewLessonSecondUIEvent(errorMsg)
    data class PhotoSuccess(@StringRes val successMsg: Int) : NewLessonSecondUIEvent(successMsg)
    data class AttachmentFail(@StringRes val errorMsg: Int) : NewLessonSecondUIEvent(errorMsg)
    data class AttachmentSuccess(@StringRes val successMsg: Int) :
        NewLessonSecondUIEvent(successMsg)

}

sealed interface NewLessonSecondAction {

    object NavigateBack : NewLessonSecondAction

    object SaveLesson : NewLessonSecondAction

    object CameraRequest : NewLessonSecondAction

    object AttachFile : NewLessonSecondAction

    object CameraRequestFail : NewLessonSecondAction

    object AttachFileFail : NewLessonSecondAction

    data class CameraRequestSuccess(val image: Bitmap) : NewLessonSecondAction
    data class AttachFileSuccess(val uri: Uri) : NewLessonSecondAction

    data class UpdateDescription(val description: String) : NewLessonSecondAction

    data class UpdateHomeworkText(val homeworkText: String) : NewLessonSecondAction

    data class AddHomeworkAttachment(val homeworkAttachment: Attachment) : NewLessonSecondAction

    data class RemoveHomeworkAttachment(val attachmentID: Int) : NewLessonSecondAction

    data class UpdateAdditionalMaterials(val additionalMaterials: String) : NewLessonSecondAction

}

data class NewLessonSecondState(
    val description: String = "",
    val homeworkText: String = "",
    val homeworkAttachments: List<Attachment>? = null,
    val additionalMaterials: String = ""
)


@HiltViewModel
class NewLessonSecondViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewLessonSecondUIState>(NewLessonSecondUIState.Loading)
    val state = _state.asStateFlow()

    private val _navigationEventsChannel = MutableSharedFlow<NewLessonSecondNavigationEvent>()
    val navigationEventsChannel = _navigationEventsChannel.asSharedFlow()

    private lateinit var firstScreenData: NewLessonFirstScreenData

    private val _uiEventsChannel = MutableSharedFlow<NewLessonSecondUIEvent>()
    val uiEventsChannel = _uiEventsChannel.asSharedFlow()
    fun passFirstScreenData(data: NewLessonFirstScreenData) {
        firstScreenData = data
        _state.value = NewLessonSecondUIState.Loading
        _state.update { NewLessonSecondUIState.Success(NewLessonSecondState()) }
    }

    fun onAction(action: NewLessonSecondAction) {
        viewModelScope.launch {
            when (action) {
                is NewLessonSecondAction.SaveLesson -> {
                    when (val state = _state.value) {
                        is NewLessonSecondUIState.Success -> {
                            /* TODO Сделать проверку времени */
                            val newLesson = NewLessonItem(
                                students = firstScreenData.students,
                                subject = firstScreenData.subject,
                                topic = firstScreenData.topic,
                                startTime = firstScreenData.startTime,
                                endTime = firstScreenData.endTime,
                                description = state.state.description,
                                homework = NewLessonHomework(
                                    text = state.state.homeworkText,
                                    attachments = state.state.homeworkAttachments,
                                ),
                                additionalMaterials = state.state.additionalMaterials,
                            )
                            withContext(Dispatchers.IO) {
                                try {
                                    newLessonRepository.saveNewLesson(newLesson)
                                    _navigationEventsChannel.emit(NewLessonSecondNavigationEvent.SaveLesson)
                                } catch (e: IOException) {
                                    /* TODO Обработать ошибку */
                                } catch (e: HttpRetryException) {
                                    /* TODO Обработать ошибку */
                                } catch (e: Exception) {
                                    /* TODO Обработать ошибку */
                                }

                            }

                        }

                        else -> {}
                    }

                }

                is NewLessonSecondAction.NavigateBack -> {
                    _navigationEventsChannel.emit(NewLessonSecondNavigationEvent.NavigateBack)
                }

                is NewLessonSecondAction.AttachFileFail -> {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_file_not_chosen))
                }

                is NewLessonSecondAction.CameraRequestFail -> {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.CameraFail(R.string.new_lesson_screen_error_no_camera_perm))
                }

                is NewLessonSecondAction.CameraRequestSuccess -> {
                    withContext(Dispatchers.IO) {
                        try {
                            val imageUrl = newLessonRepository.uploadImage(action.image)
                            val imageAttachment = Attachment.Image(
                                url = imageUrl,
                                description = null
                            )

                            onAction(NewLessonSecondAction.AddHomeworkAttachment(imageAttachment))
                        } catch (e: IOException) {
                            _uiEventsChannel.emit(NewLessonSecondUIEvent.CameraFail(R.string.new_lesson_screen_error_io_upload_image))
                        } catch (e: HttpRetryException) {
                            _uiEventsChannel.emit(NewLessonSecondUIEvent.CameraFail(R.string.new_lesson_screen_error_io_upload_image))
                        } catch (e: Exception) {
                            _uiEventsChannel.emit(NewLessonSecondUIEvent.CameraFail(R.string.new_lesson_screen_error_io_upload_image))
                        }
                    }
                }

                is NewLessonSecondAction.AttachFileSuccess -> {
                    /* TODO Обработка добавленного файла */
                }

                else -> {
                    _state.update { state ->
                        if (state is NewLessonSecondUIState.Success) {
                            when (action) {
                                is NewLessonSecondAction.AddHomeworkAttachment -> addHomeworkAttachment(
                                    state,
                                    action.homeworkAttachment
                                )

                                is NewLessonSecondAction.RemoveHomeworkAttachment -> removeHomeworkAttachment(
                                    state,
                                    action.attachmentID
                                )

                                is NewLessonSecondAction.UpdateAdditionalMaterials -> updateAdditionalMaterials(
                                    state,
                                    action.additionalMaterials
                                )

                                is NewLessonSecondAction.UpdateDescription -> updateDescription(
                                    state,
                                    action.description
                                )

                                is NewLessonSecondAction.UpdateHomeworkText -> updateHomeworkText(
                                    state,
                                    action.homeworkText
                                )

                                else -> state
                            }
                        } else {
                            state
                        }
                    }
                }
            }
        }
    }

    private fun addHomeworkAttachment(
        state: NewLessonSecondUIState.Success,
        attachment: Attachment
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                homeworkAttachments = (state.state.homeworkAttachments ?: emptyList()) + attachment
            )
        )
    }

    private fun removeHomeworkAttachment(
        state: NewLessonSecondUIState.Success,
        attachmentID: Int
    ): NewLessonSecondUIState {
        val homeworkAttachments = state.state.homeworkAttachments
        return if (homeworkAttachments != null && attachmentID < homeworkAttachments.size) {
            state.copy(
                state = state.state.copy(
                    homeworkAttachments = homeworkAttachments.minusElement(homeworkAttachments[attachmentID])
                )
            )
        } else {
            state
        }
    }

    private fun updateAdditionalMaterials(
        state: NewLessonSecondUIState.Success,
        additionalMaterials: String
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                additionalMaterials = additionalMaterials
            )
        )
    }

    private fun updateDescription(
        state: NewLessonSecondUIState.Success,
        description: String
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                description = description
            )
        )
    }

    private fun updateHomeworkText(
        state: NewLessonSecondUIState.Success,
        homeworkText: String
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                homeworkText = homeworkText
            )
        )
    }
}
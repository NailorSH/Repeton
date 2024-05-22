package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Attachment
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
import java.time.LocalDateTime
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

    data class StartTimeError(@StringRes val errorMsg: Int) : NewLessonSecondUIEvent(errorMsg)

}

sealed interface NewLessonSecondAction {

    object NavigateBack : NewLessonSecondAction

    object SaveLesson : NewLessonSecondAction

    object CameraRequest : NewLessonSecondAction

    object AttachFile : NewLessonSecondAction

    object CameraRequestFail : NewLessonSecondAction

    object AttachFileFail : NewLessonSecondAction

    data class CameraRequestSuccess(val uri: Uri) : NewLessonSecondAction
    data class AttachFileSuccess(val uri: Uri) : NewLessonSecondAction

    data class UpdateDescription(val description: String) : NewLessonSecondAction

    data class UpdateHomeworkText(val homeworkText: String) : NewLessonSecondAction

    data class UpdateImageText(val imageText: String, val imageIndex: Int) : NewLessonSecondAction

    data class AddImageAttachment(val addImageAttachment: Uri) : NewLessonSecondAction

    data class AddFileAttachment(val fileAttachments: Attachment.File) : NewLessonSecondAction

    data class RemoveImageAttachment(val imageIndex: Int) : NewLessonSecondAction

    data class UpdateAdditionalMaterials(val additionalMaterials: String) : NewLessonSecondAction

    data class UpdateShowImageTypeDialogue(val enableDialogue: Boolean) : NewLessonSecondAction

    data class UpdateShowImageDialogue(val enableDialogue: Boolean) : NewLessonSecondAction

}

data class NewLessonSecondState(
    val description: String = "",
    val homeworkText: String = "",
    val imageAttachments: List<Attachment.Image> = emptyList(),
    val fileAttachments: List<Attachment.File> = emptyList(),
    val additionalMaterials: String = "",
    val showImageTypeDialogue: Boolean = false,
    val showImageDialogue: Boolean = false,
)


@HiltViewModel
class NewLessonSecondViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
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
        if (_state.value !is NewLessonSecondUIState.Success) {
            _state.value = NewLessonSecondUIState.Loading
            _state.update { NewLessonSecondUIState.Success(NewLessonSecondState()) }
        }
    }

    fun onAction(action: NewLessonSecondAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (action) {
                is NewLessonSecondAction.SaveLesson -> {
                    when (val state = _state.value) {
                        is NewLessonSecondUIState.Success -> {
                            /* TODO Сделать проверку времени */
                            if (!checkStartTime(firstScreenData.startTime)) {
                                return@launch
                            }
                            val imageURLsList: List<String>
                            try {
                                imageURLsList =
                                    newLessonRepository.uploadImages(state.state.imageAttachments)
                            } catch (e: IOException) {
                                _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_upload_file))
                                return@launch
                            } catch (e: HttpRetryException) {
                                _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_upload_file))
                                return@launch
                            } catch (e: Exception) {
                                _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_upload_file))
                                return@launch
                            }

                            val newLesson = NewLessonItem(
                                students = firstScreenData.students,
                                subject = firstScreenData.subject,
                                topic = firstScreenData.topic,
                                startTime = firstScreenData.startTime,
                                endTime = firstScreenData.endTime,
                                description = state.state.description,
                                homework = NewLessonHomework(
                                    text = state.state.homeworkText,
                                    attachments = state.state.imageAttachments.mapIndexed { index, image ->
                                        image.copy(url = imageURLsList[index])
                                    },
                                ),
                                additionalMaterials = state.state.additionalMaterials,
                            )

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

                is NewLessonSecondAction.AttachFileSuccess -> {
                    withContext(Dispatchers.IO) {
                        try {
                            val fileUri = action.uri
                            val fileUrl = newLessonRepository.uploadFile(fileUri)
                            val fileName = getFilenameFromUri(fileUri) ?: "Неизвестный файл"

                            val fileAttachment = Attachment.File(
                                url = fileUrl,
                                fileName = fileName
                            )

                            onAction(NewLessonSecondAction.AddFileAttachment(fileAttachment))
                        } catch (e: Exception) {
                            /* TODO */
                        }
                    }
                }

                else -> {
                    _state.update { state ->
                        if (state is NewLessonSecondUIState.Success) {
                            when (action) {
                                is NewLessonSecondAction.UpdateImageText -> updateImageText(
                                    state,
                                    action.imageText,
                                    action.imageIndex
                                )

                                is NewLessonSecondAction.AddFileAttachment -> addFileAttachment(
                                    state,
                                    action.fileAttachments
                                )

                                is NewLessonSecondAction.AddImageAttachment -> addImageAttachment(
                                    state,
                                    action.addImageAttachment
                                )

                                is NewLessonSecondAction.RemoveImageAttachment -> removeImageAttachment(
                                    state,
                                    action.imageIndex
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

                                is NewLessonSecondAction.UpdateShowImageTypeDialogue -> updateShowImageTypeDialogue(
                                    state,
                                    action.enableDialogue
                                )

                                is NewLessonSecondAction.UpdateShowImageDialogue -> updateShowImageDialogue(
                                    state,
                                    action.enableDialogue
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

    private suspend fun handleSaveLesson() {

    }

    private suspend fun checkStartTime(startTime: LocalDateTime): Boolean {
        return if (startTime >= LocalDateTime.now()) {
            true
        } else {
            _uiEventsChannel.emit(NewLessonSecondUIEvent.StartTimeError(R.string.new_lesson_screen_start_time_error))
            _navigationEventsChannel.emit(NewLessonSecondNavigationEvent.NavigateBack)
            false
        }
    }

    private fun addImageAttachment(
        state: NewLessonSecondUIState.Success,
        attachment: Uri
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                imageAttachments = state.state.imageAttachments.plus(
                    Attachment.Image(
                        url = attachment.toString()
                    )
                )
            )
        )
    }

    private fun addFileAttachment(
        state: NewLessonSecondUIState.Success,
        attachment: Attachment.File
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                fileAttachments = state.state.fileAttachments.plus(attachment)
            )
        )
    }

    private fun removeImageAttachment(
        state: NewLessonSecondUIState.Success,
        attachmentID: Int
    ): NewLessonSecondUIState {
        val homeworkAttachments = state.state.imageAttachments
        return try {
            state.copy(
                state = state.state.copy(
                    imageAttachments = homeworkAttachments.minusElement(homeworkAttachments[attachmentID])
                )
            )
        } catch (e: IndexOutOfBoundsException) {
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

    private fun updateImageText(
        state: NewLessonSecondUIState.Success,
        imageText: String,
        imageIndex: Int,
    ): NewLessonSecondUIState {
        return if (imageIndex < state.state.imageAttachments.size) {
            state.copy(
                state = state.state.copy(
                    imageAttachments = state.state.imageAttachments.toMutableList().apply {
                        this[imageIndex] = this[imageIndex].copy(description = imageText)
                    }
                )
            )
        } else {
            state
        }
    }

    private fun updateShowImageTypeDialogue(
        state: NewLessonSecondUIState.Success,
        dialogueEnabled: Boolean
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                showImageTypeDialogue = dialogueEnabled
            )
        )
    }

    private fun updateShowImageDialogue(
        state: NewLessonSecondUIState.Success,
        dialogueEnabled: Boolean
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                showImageDialogue = dialogueEnabled
            )
        )
    }


    private fun getFilenameFromUri(uri: Uri): String? {
        val path = uri.path
        val cut = path?.lastIndexOf('/')
        return if (cut != null && cut != -1) path.substring(cut + 1) else null
    }
}
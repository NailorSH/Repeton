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
    data class LessonSaveFail(@StringRes val successMsg: Int) : NewLessonSecondUIEvent(successMsg)
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
    object TooMuchImages : NewLessonSecondAction

    data class CameraRequestSuccess(val uri: Uri) : NewLessonSecondAction
    data class AttachFileSuccess(val uri: Uri) : NewLessonSecondAction

    data class UpdateDescription(val description: String) : NewLessonSecondAction

    data class UpdateHomeworkText(val homeworkText: String) : NewLessonSecondAction

    data class UpdateImageText(val imageText: String, val imageIndex: Int) : NewLessonSecondAction

    data class AddImageAttachment(val addImageAttachment: Uri) : NewLessonSecondAction

    data class AddFileAttachment(val fileAttachments: Attachment.File) : NewLessonSecondAction

    data class RemoveImageAttachment(val attachment: Attachment.Image) : NewLessonSecondAction

    data class UpdateAdditionalMaterials(val additionalMaterials: String) : NewLessonSecondAction

    data class UpdateShowImageTypeDialogue(val enableDialogue: Boolean) : NewLessonSecondAction

    data class UpdateShowImageDialogue(val enableDialogue: Boolean) : NewLessonSecondAction

    data class UpdateShowLoadingDialogue(val enableDialogue: Boolean) : NewLessonSecondAction
}


data class NewLessonSecondState(
    val description: String = "",
    val homeworkText: String = "",
    val imageAttachments: MutableList<Attachment.Image> = mutableListOf(
        Attachment.Image(
            url = ""
        )
    ),
    val fileAttachments: List<Attachment.File> = emptyList(),
    val additionalMaterials: String = "",
    val showImageTypeDialogue: Boolean = false,
    val showImageDialogue: Boolean = false,
    val showImageSlider: Boolean = false,


    val showLoadingDialogue: Boolean = false,
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
        _state.value = NewLessonSecondUIState.Loading
        _state.update { NewLessonSecondUIState.Success(NewLessonSecondState()) }
    }

    fun onAction(action: NewLessonSecondAction) {
        viewModelScope.launch {
            when (action) {

                is NewLessonSecondAction.SaveLesson -> {
                    if (_state.value is NewLessonSecondUIState.Success &&
                        !(_state.value as NewLessonSecondUIState.Success).state.showLoadingDialogue) {
                        onAction(NewLessonSecondAction.UpdateShowLoadingDialogue(true))
                        saveLesson()
                        onAction(NewLessonSecondAction.UpdateShowLoadingDialogue(false))
                    }
                }


                is NewLessonSecondAction.NavigateBack -> {
                    _navigationEventsChannel.emit(NewLessonSecondNavigationEvent.NavigateBack)
                }

                is NewLessonSecondAction.TooMuchImages -> {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_too_much_images_error))
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
                                    action.attachment
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

                                is NewLessonSecondAction.UpdateShowLoadingDialogue -> updateShowLoadingDialogue(
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

    private suspend fun saveLesson() {
        when (val state = _state.value) {
            is NewLessonSecondUIState.Success -> {
                val isTutor: Boolean = try {
                    newLessonRepository.getUserType()
                } catch (e: Exception) {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.LessonSaveFail(R.string.new_lesson_error_cant_get_user_status))
                    return
                }
                if (!isTutor) {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_not_tutor))
                    return
                }
                if (!checkStartTime(firstScreenData.startTime)) {
                    return
                }
                var imageURLsList: List<String> = listOf()
                if (state.state.showImageSlider) {
                    try {
                        imageURLsList =
                            newLessonRepository.uploadImages(state.state.imageAttachments)
                    } catch (e: IOException) {
                        _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_upload_file))
                        return
                    } catch (e: HttpRetryException) {
                        _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_upload_file))
                        return
                    } catch (e: Exception) {
                        _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_upload_file))
                        return
                    }
                }

                val imageAttachments =
                    if (state.state.showImageSlider) state.state.imageAttachments else null
                val newLesson = NewLessonItem(
                    students = firstScreenData.students,
                    subject = firstScreenData.subject,
                    topic = firstScreenData.topic,
                    startTime = firstScreenData.startTime,
                    endTime = firstScreenData.endTime,
                    description = state.state.description,
                    homework = NewLessonHomework(
                        text = state.state.homeworkText,
                        attachments = imageAttachments?.mapIndexed { index, image ->
                            image.copy(url = imageURLsList[index])
                        },
                    ),
                    additionalMaterials = state.state.additionalMaterials,
                )

                try {
                    newLessonRepository.saveNewLesson(newLesson)
                    _navigationEventsChannel.emit(NewLessonSecondNavigationEvent.SaveLesson)
                } catch (e: IOException) {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.LessonSaveFail(R.string.new_lesson_screen_error_lesson_save))
                } catch (e: HttpRetryException) {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.LessonSaveFail(R.string.new_lesson_screen_error_lesson_save))
                } catch (e: Exception) {
                    _uiEventsChannel.emit(NewLessonSecondUIEvent.LessonSaveFail(R.string.new_lesson_screen_error_lesson_save))
                }
            }

            else -> {}

        }
    }

    private suspend fun checkStartTime(startTime: LocalDateTime): Boolean {
        return if (startTime >= LocalDateTime.now()) {
            true
        } else {
            _uiEventsChannel.emit(NewLessonSecondUIEvent.StartTimeError(R.string.new_lesson_screen_start_time_error))
            false
        }
    }

    private suspend fun addImageAttachment(
        state: NewLessonSecondUIState.Success,
        attachment: Uri
    ): NewLessonSecondUIState {
        val newImage = Attachment.Image(
            url = attachment.toString()
        )
        return if (state.state.imageAttachments.size == 10) {
            _uiEventsChannel.emit(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_too_much_images_error))
            state
        } else if (!state.state.showImageSlider) {
            state.copy(
                state = state.state.copy(
                    imageAttachments = mutableListOf(newImage),
                    showImageSlider = true
                )
            )
        } else {
            val newList = state.state.imageAttachments.toMutableList()
            newList.add(newImage)
            state.copy(
                state = state.state.copy(
                    imageAttachments = newList,
                    showImageSlider = true,
                ),
            )
        }

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

    private suspend fun removeImageAttachment(
        state: NewLessonSecondUIState.Success,
        attachment: Attachment.Image
    ): NewLessonSecondUIState {
        return if (state.state.imageAttachments.size <= 1) {
            state.copy(
                state = state.state.copy(
                    showImageSlider = false
                )
            )
        } else {
            val newList = state.state.imageAttachments.toMutableList()
            newList.remove(attachment)
            state.copy(
                state = state.state.copy(
                    imageAttachments = newList
                )
            )
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
            val newList = state.state.imageAttachments.toMutableList()
            newList[imageIndex] = newList[imageIndex].copy(description = imageText)
            state.copy(
                state = state.state.copy(
                    imageAttachments = newList
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

    private fun updateShowLoadingDialogue(
        state: NewLessonSecondUIState.Success,
        dialogueEnabled: Boolean
    ): NewLessonSecondUIState {
        return state.copy(
            state = state.state.copy(
                showLoadingDialogue = dialogueEnabled
            )
        )
    }


    private fun getFilenameFromUri(uri: Uri): String? {
        val path = uri.path
        val cut = path?.lastIndexOf('/')
        return if (cut != null && cut != -1) path.substring(cut + 1) else null
    }
}
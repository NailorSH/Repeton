package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonFirstScreenData
import com.nailorsh.repeton.features.newlesson.data.repository.NewLessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    data class CameraSuccess(@StringRes val successMsg: Int) : NewLessonSecondUIEvent(successMsg)
    data class AttachmentFail(@StringRes val errorMsg: Int) : NewLessonSecondUIEvent(errorMsg)
    data class AttachmentSuccess(@StringRes val successMsg: Int) :
        NewLessonSecondUIEvent(successMsg)

}

sealed interface NewLessonSecondCallBack {

    object NavigateBack : NewLessonSecondCallBack

    object SaveLesson : NewLessonSecondCallBack

    object CameraRequest : NewLessonSecondCallBack

    object AttachFile : NewLessonSecondCallBack

    object CameraRequestFail : NewLessonSecondCallBack

    object AttachFileFail : NewLessonSecondCallBack

    data class CameraRequestSuccess(val image: Bitmap) : NewLessonSecondCallBack
    data class AttachFileSuccess(val uri: Uri) : NewLessonSecondCallBack

    data class UpdateDescription(val description: String) : NewLessonSecondCallBack

    data class UpdateHomeworkText(val homeworkText: String) : NewLessonSecondCallBack

    data class AddHomeworkAttachment(val homeworkAttachment: Attachment) : NewLessonSecondCallBack

    data class RemoveHomeworkAttachment(val attachmentID: Int) : NewLessonSecondCallBack

    data class UpdateAdditionalMaterials(val additionalMaterials: String) : NewLessonSecondCallBack

}

data class NewLessonSecondState(
    val description: String = "",
    val homeworkText: String = "",
    val homeworkAttachments: List<Attachment>? = null,
    val additionalMaterials: String = ""
)


@HiltViewModel
class NewLessonSecondViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewLessonSecondUIState>(NewLessonSecondUIState.Loading)
    val state = _state.asStateFlow()

    private val _navigationEventsChannel = Channel<NewLessonSecondNavigationEvent>()
    val navigationEventsChannel = _navigationEventsChannel.receiveAsFlow()

    private lateinit var firstScreenData: NewLessonFirstScreenData

    private val _uiEventsChannel = Channel<NewLessonSecondUIEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()
    fun passFirstScreenData(data: NewLessonFirstScreenData) {
        firstScreenData = data
        _state.value = NewLessonSecondUIState.Loading
        _state.update { NewLessonSecondUIState.Success(NewLessonSecondState()) }
    }

    fun onCallback(callback: NewLessonSecondCallBack) {
        viewModelScope.launch {
            when (callback) {
                is NewLessonSecondCallBack.SaveLesson -> {
                    when (val state = _state.value) {
                        is NewLessonSecondUIState.Success -> {
                            /* TODO Сделать проверку времени */
                            val newLesson = Lesson(
                                id = Id("0"),
                                subject = firstScreenData.subject,
                                topic = firstScreenData.topic,
                                startTime = firstScreenData.startTime,
                                endTime = firstScreenData.endTime,
                                tutor = FakeTutorsSource.getTutorById(Id("1")),
                                description = state.state.description,
                                homework = Homework(
                                    text = state.state.homeworkText,
                                    attachments = state.state.homeworkAttachments
                                ),
                                additionalMaterials = state.state.additionalMaterials
                            )
                            newLessonRepository.saveNewLesson(newLesson)
                            _navigationEventsChannel.send(NewLessonSecondNavigationEvent.SaveLesson)
                        }

                        else -> {}
                    }

                }

                is NewLessonSecondCallBack.NavigateBack -> {
                    _navigationEventsChannel.send(NewLessonSecondNavigationEvent.NavigateBack)
                }

                is NewLessonSecondCallBack.AttachFileFail -> {
                    _uiEventsChannel.send(NewLessonSecondUIEvent.AttachmentFail(R.string.new_lesson_screen_error_file_not_chosen))
                }

                is NewLessonSecondCallBack.CameraRequestFail -> {
                    _uiEventsChannel.send(NewLessonSecondUIEvent.CameraFail(R.string.new_lessson_screen_error_no_camera_perm))
                }

                is NewLessonSecondCallBack.CameraRequestSuccess -> {
                    /* TODO Обработка изображения с камеры */
                }

                is NewLessonSecondCallBack.AttachFileSuccess -> {
                    /* TODO Обработка добавленного файла */
                }

                else -> {
                    _state.update { state ->
                        if (state is NewLessonSecondUIState.Success) {
                            when (callback) {
                                is NewLessonSecondCallBack.AddHomeworkAttachment -> addHomeworkAttachment(
                                    state,
                                    callback.homeworkAttachment
                                )

                                is NewLessonSecondCallBack.RemoveHomeworkAttachment -> removeHomeworkAttachment(
                                    state,
                                    callback.attachmentID
                                )

                                is NewLessonSecondCallBack.UpdateAdditionalMaterials -> updateAdditionalMaterials(
                                    state,
                                    callback.additionalMaterials
                                )

                                is NewLessonSecondCallBack.UpdateDescription -> updateDescription(
                                    state,
                                    callback.description
                                )

                                is NewLessonSecondCallBack.UpdateHomeworkText -> updateHomeworkText(
                                    state,
                                    callback.homeworkText
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
        /* TODO verify and adding attachment logic */
        return state
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


package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


sealed interface NewLessonFirstUiState {
    data class Success(val state: NewLessonFirstState) : NewLessonFirstUiState
    object Error : NewLessonFirstUiState
    object Loading : NewLessonFirstUiState

}

sealed interface NewLessonCallback {

    object NavigateBack : NewLessonCallback
    object SaveData : NewLessonCallback
    data class UpdateSubjectText(val subjectText: String) : NewLessonCallback
    data class UpdateTopicText(val topic: String) : NewLessonCallback
    data class UpdateDate(val date: LocalDate) : NewLessonCallback
    data class UpdateStartTime(val startTime: LocalDateTime) : NewLessonCallback
    data class UpdateEndTime(val endTime: LocalDateTime) : NewLessonCallback
    data class UpdateShowDatePicker(val datePickerEnabled: Boolean) : NewLessonCallback
    data class UpdateShowTimePickerStart(val timePickerStartEnabled: Boolean) : NewLessonCallback
    data class UpdateShowTimePickerEnd(val timePickerEndEnabled: Boolean) : NewLessonCallback

    data class UpdateShowDropDownMenu(val dropDownMenuEnabled: Boolean) : NewLessonCallback

    data class UpdateShowTimePickerStartTextField(val timePickerStartTextFieldEnabled: Boolean) :
        NewLessonCallback

    data class UpdateShowTimePickerEndTextField(val timePickerEndTextFieldEnabled: Boolean) :
        NewLessonCallback

//    data class DropDownMenuItemChoose(val itemID: Int) : NewLessonCallback
}

sealed interface NewLessonUIEvent {

    object SubjectError : NewLessonUIEvent

    object TopicError : NewLessonUIEvent

    object DateError : NewLessonUIEvent

    object StartTimeError : NewLessonUIEvent

    object EndTimeError : NewLessonUIEvent

}


sealed interface NewLessonNavigationEvent {
    data class NavigateToNext(
        val subject: Subject,
        val topic: String,
        val startTime: LocalDateTime,
        val endTime: LocalDateTime
    ) : NewLessonNavigationEvent

    object NavigateBack : NewLessonNavigationEvent
}


data class NewLessonFirstState(

    val subject: Subject = Subject.None,
    val topic: String = "",

    val startTime: LocalDateTime = LocalDateTime.now().plusMinutes(1),
    val endTime: LocalDateTime = LocalDateTime.now().plusMinutes(30),

    val subjectText: String = "",
    val loadedSubjects: List<String> = emptyList(),

    val showDropdownMenu: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePickerStart: Boolean = false,
    val showTimePickerEnd: Boolean = false,

    val showTimePickerStartTextField: Boolean = false,
    val showTimePickerEndTextField: Boolean = false
)

@HiltViewModel
class NewLessonViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewLessonFirstUiState>(NewLessonFirstUiState.Loading)
    val state: StateFlow<NewLessonFirstUiState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<NewLessonUIEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    private val _navigationEventsChannel = Channel<NewLessonNavigationEvent>()
    val navigationEventsChannel = _navigationEventsChannel.receiveAsFlow()

    init {
        clearData()
    }

    fun clearData() {
        viewModelScope.launch {
            _state.value = NewLessonFirstUiState.Loading
            _state.update {
                try {
                    val subjects = newLessonRepository.getSubjects("")
                    NewLessonFirstUiState.Success(NewLessonFirstState(loadedSubjects = subjects))
                } catch (e: IOException) {
                    NewLessonFirstUiState.Error
                } catch (e: HttpRetryException) {
                    NewLessonFirstUiState.Error
                }
            }
        }

    }


    private suspend fun onSave() {
        if (_state.value is NewLessonFirstUiState.Success) {
            val state = (_state.value as NewLessonFirstUiState.Success).state
            val subject = newLessonRepository.getSubject(state.subjectText)
            if (subject == null) {
                _uiEventsChannel.send(NewLessonUIEvent.SubjectError)
                return
            }
            if (state.topic.isBlank()) {
                _uiEventsChannel.send(NewLessonUIEvent.TopicError)
                return
            }
            if (checkDate(state.startTime.toLocalDate()) && checkStartTime(state.startTime) && checkEndTime(
                    startTime = state.startTime,
                    endTime = state.endTime
                )
            ) {
                _navigationEventsChannel.send(
                    NewLessonNavigationEvent.NavigateToNext(
                        subject = subject,
                        topic = state.topic,
                        startTime = state.startTime,
                        endTime = state.endTime
                    )
                )
            }
        }

    }


    private suspend fun checkStartTime(startTime: LocalDateTime): Boolean {
        return if (startTime >= LocalDateTime.now()) {
            true
        } else {
            _uiEventsChannel.send(NewLessonUIEvent.StartTimeError)
            false
        }
    }

    private suspend fun checkEndTime(startTime: LocalDateTime, endTime: LocalDateTime): Boolean {
        return if (startTime < endTime) {
            true
        } else {
            _uiEventsChannel.send(NewLessonUIEvent.EndTimeError)
            false
        }
    }

    private suspend fun checkDate(date: LocalDate): Boolean {
        return if (date >= LocalDate.now()) {
            true
        } else {
            _uiEventsChannel.send(NewLessonUIEvent.DateError)
            false
        }
    }

    fun onCallback(callback: NewLessonCallback) {
        viewModelScope.launch {
            if (callback is NewLessonCallback.SaveData) {
                onSave()
            } else if (callback is NewLessonCallback.NavigateBack) {
                _navigationEventsChannel.send(NewLessonNavigationEvent.NavigateBack)
            } else {
                _state.update {
                    when (val state = _state.value) {
                        is NewLessonFirstUiState.Success -> {
                            when (callback) {
                                is NewLessonCallback.UpdateDate -> updateDate(
                                    state,
                                    callback.date
                                )

                                is NewLessonCallback.UpdateEndTime -> updateEndTime(
                                    state,
                                    callback.endTime
                                )

                                is NewLessonCallback.UpdateStartTime -> updateStartTime(
                                    state,
                                    callback.startTime
                                )

                                is NewLessonCallback.UpdateSubjectText -> updateSubjectText(
                                    state,
                                    callback.subjectText
                                )

                                is NewLessonCallback.UpdateTopicText -> updateTopic(
                                    state,
                                    callback.topic
                                )

                                is NewLessonCallback.UpdateShowDatePicker -> updateDatePickerState(
                                    state,
                                    callback.datePickerEnabled
                                )

                                is NewLessonCallback.UpdateShowTimePickerEnd -> updateTimePickerEndState(
                                    state,
                                    callback.timePickerEndEnabled
                                )

                                is NewLessonCallback.UpdateShowTimePickerStart -> updateTimePickerStartState(
                                    state,
                                    callback.timePickerStartEnabled
                                )

                                is NewLessonCallback.UpdateShowDropDownMenu -> updateDropDownMenuState(
                                    state,
                                    callback.dropDownMenuEnabled
                                )

                                is NewLessonCallback.UpdateShowTimePickerStartTextField -> updateTimePickerStartTextFieldState(
                                    state,
                                    callback.timePickerStartTextFieldEnabled
                                )

                                is NewLessonCallback.UpdateShowTimePickerEndTextField -> updateTimePickerEndTextFieldState(
                                    state,
                                    callback.timePickerEndTextFieldEnabled
                                )

                                else -> state
                            }
                        }

                        else -> it
                    }
                }
            }
        }

    }


    private suspend fun updateDate(
        state: NewLessonFirstUiState.Success,
        date: LocalDate
    ): NewLessonFirstUiState {
        return if (checkDate(date)) {
            val startTime = date.atStartOfDay().withHour(LocalTime.now().hour)
                .withMinute(LocalTime.now().minute).plusMinutes(1)
            val endTime = startTime.plusMinutes(30)
            state.copy(
                state = state.state.copy(
                    startTime = startTime,
                    endTime = endTime,
                    showDatePicker = false,
                    showTimePickerStart = true,
                    showTimePickerStartTextField = true
                )
            )
        } else {
            state
        }
    }


    private suspend fun updateEndTime(
        state: NewLessonFirstUiState.Success,
        endTime: LocalDateTime
    ): NewLessonFirstUiState {
        return if (checkEndTime(endTime = endTime, startTime = state.state.startTime)) {
            state.copy(state = state.state.copy(endTime = endTime, showTimePickerEnd = false))
        } else {
            state
        }
    }

    private suspend fun updateStartTime(
        state: NewLessonFirstUiState.Success,
        startTime: LocalDateTime
    ): NewLessonFirstUiState {
        return if (checkStartTime(startTime)) {
            state.copy(
                state = state.state.copy(
                    startTime = startTime,
                    endTime = startTime.plusMinutes(30),
                    showTimePickerStart = false,
                    showTimePickerEnd = true,
                    showTimePickerEndTextField = true
                )
            )
        } else {
            state
        }
    }

    private suspend fun updateSubjectText(
        state: NewLessonFirstUiState.Success,
        subjectText: String
    ): NewLessonFirstUiState {
        val filteredSubjects = newLessonRepository.getSubjects(subjectText)
        return state.copy(
            state = state.state.copy(
                subjectText = subjectText,
                loadedSubjects = filteredSubjects
            )
        )
    }

    private fun updateTopic(
        state: NewLessonFirstUiState.Success,
        topic: String
    ): NewLessonFirstUiState {
        return state.copy(
            state = state.state.copy(
                topic = topic
            )
        )
    }

    private fun updateDatePickerState(
        state: NewLessonFirstUiState.Success,
        datePickerEnabled: Boolean
    ): NewLessonFirstUiState {
        return state.copy(state = state.state.copy(showDatePicker = datePickerEnabled))
    }

    private fun updateTimePickerStartState(
        state: NewLessonFirstUiState.Success,
        timePickerStartEnabled: Boolean
    ): NewLessonFirstUiState {
        return state.copy(state = state.state.copy(showTimePickerStart = timePickerStartEnabled))
    }

    private fun updateTimePickerEndState(
        state: NewLessonFirstUiState.Success,
        timePickerEndEnabled: Boolean
    ): NewLessonFirstUiState {
        return state.copy(state = state.state.copy(showTimePickerEnd = timePickerEndEnabled))
    }

    private fun updateDropDownMenuState(
        state: NewLessonFirstUiState.Success,
        dropDownMenuEnabled: Boolean
    ): NewLessonFirstUiState {
        return state.copy(state = state.state.copy(showDropdownMenu = dropDownMenuEnabled))
    }

    private fun updateTimePickerStartTextFieldState(
        state: NewLessonFirstUiState.Success,
        timePickerStartTextFieldEnabled: Boolean
    ): NewLessonFirstUiState {
        return state.copy(state = state.state.copy(showTimePickerStartTextField = timePickerStartTextFieldEnabled))
    }

    private fun updateTimePickerEndTextFieldState(
        state: NewLessonFirstUiState.Success,
        timePickerEndTextFieldEnabled: Boolean
    ): NewLessonFirstUiState {
        return state.copy(state = state.state.copy(showTimePickerEndTextField = timePickerEndTextFieldEnabled))
    }


}

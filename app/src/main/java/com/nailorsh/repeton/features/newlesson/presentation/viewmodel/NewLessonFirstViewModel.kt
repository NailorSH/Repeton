package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonFirstScreenData
import com.nailorsh.repeton.features.newlesson.data.repository.NewLessonRepository
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


sealed interface NewLessonFirstUIState {
    data class Success(val state: NewLessonFirstState) : NewLessonFirstUIState
    object Error : NewLessonFirstUIState
    object Loading : NewLessonFirstUIState

}

sealed interface NewLessonFirstCallback {

    object NavigateBack : NewLessonFirstCallback
    object SaveData : NewLessonFirstCallback
    data class UpdateSubjectText(val subjectText: String) : NewLessonFirstCallback
    data class UpdateTopicText(val topic: String) : NewLessonFirstCallback
    data class UpdateDate(val date: LocalDate) : NewLessonFirstCallback
    data class UpdateStartTime(val startTime: LocalDateTime) : NewLessonFirstCallback
    data class UpdateEndTime(val endTime: LocalDateTime) : NewLessonFirstCallback
    data class UpdateShowDatePicker(val datePickerEnabled: Boolean) : NewLessonFirstCallback
    data class UpdateShowTimePickerStart(val timePickerStartEnabled: Boolean) : NewLessonFirstCallback
    data class UpdateShowTimePickerEnd(val timePickerEndEnabled: Boolean) : NewLessonFirstCallback

    data class UpdateShowDropDownMenu(val dropDownMenuEnabled: Boolean) : NewLessonFirstCallback

    data class UpdateShowTimePickerStartTextField(val timePickerStartTextFieldEnabled: Boolean) :
        NewLessonFirstCallback

    data class UpdateShowTimePickerEndTextField(val timePickerEndTextFieldEnabled: Boolean) :
        NewLessonFirstCallback

}

sealed class NewLessonUIEvent(@StringRes val errorMsg: Int) {

    object SubjectError : NewLessonUIEvent(R.string.new_lesson_screen_subject_error)

    object TopicError : NewLessonUIEvent(R.string.new_lesson_screen_topic_error)

    object DateError : NewLessonUIEvent(R.string.new_lesson_screen_date_error)

    object StartTimeError : NewLessonUIEvent(R.string.new_lesson_screen_start_time_error)

    object EndTimeError : NewLessonUIEvent(R.string.new_lesson_screen_end_time_error)

}


sealed interface NewLessonFirstNavigationEvent {
    data class NavigateToNext(
        val firstScreenData: NewLessonFirstScreenData
    ) : NewLessonFirstNavigationEvent

    object NavigateBack : NewLessonFirstNavigationEvent
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
    val showTimePickerEndTextField: Boolean = false,

    )

@HiltViewModel
class NewLessonFirstViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewLessonFirstUIState>(NewLessonFirstUIState.Loading)
    val state: StateFlow<NewLessonFirstUIState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<NewLessonUIEvent>()
    val uiEventsChannel = _uiEventsChannel.receiveAsFlow()

    private val _navigationEventsChannel = Channel<NewLessonFirstNavigationEvent>()
    val navigationEventsChannel = _navigationEventsChannel.receiveAsFlow()

    init {
        clearData()
    }

    fun clearData() {
        viewModelScope.launch {
            _state.value = NewLessonFirstUIState.Loading
            _state.update {
                try {
                    val subjects = newLessonRepository.getSubjects("")
                    NewLessonFirstUIState.Success(NewLessonFirstState(loadedSubjects = subjects))
                } catch (e: IOException) {
                    NewLessonFirstUIState.Error
                } catch (e: HttpRetryException) {
                    NewLessonFirstUIState.Error
                }
            }
        }

    }


    private suspend fun onSave() {
        if (_state.value is NewLessonFirstUIState.Success) {
            val state = (_state.value as NewLessonFirstUIState.Success).state
            val subject = newLessonRepository.getSubject(state.subjectText)
            if (!checkSubject(subject)) {
                return
            }
            if (!checkTopic(state.topic)) {
                return
            }
            if (checkDate(state.startTime.toLocalDate()) && checkStartTime(state.startTime) && checkEndTime(
                    startTime = state.startTime,
                    endTime = state.endTime
                )
            ) {
                _navigationEventsChannel.send(
                    NewLessonFirstNavigationEvent.NavigateToNext(
                        NewLessonFirstScreenData(
                            subject = subject!!,
                            topic = state.topic,
                            startTime = state.startTime,
                            endTime = state.endTime
                        )
                    )
                )
            }
        }

    }

    private suspend fun checkTopic(topic: String): Boolean {
        return if (topic.isNotBlank()) {
            true
        } else {
            _uiEventsChannel.send(NewLessonUIEvent.TopicError)
            false
        }
    }

    private suspend fun checkSubject(subject: Subject?): Boolean {
        return if (subject != null) {
            true
        } else {
            _uiEventsChannel.send(NewLessonUIEvent.SubjectError)
            false
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

    fun onCallback(callback: NewLessonFirstCallback) {
        viewModelScope.launch {
            if (callback is NewLessonFirstCallback.SaveData) {
                onSave()
            } else if (callback is NewLessonFirstCallback.NavigateBack) {
                _navigationEventsChannel.send(NewLessonFirstNavigationEvent.NavigateBack)
            } else {
                _state.update {
                    when (val state = it) {
                        is NewLessonFirstUIState.Success -> {
                            when (callback) {
                                is NewLessonFirstCallback.UpdateDate -> updateDate(
                                    state,
                                    callback.date
                                )

                                is NewLessonFirstCallback.UpdateEndTime -> updateEndTime(
                                    state,
                                    callback.endTime
                                )

                                is NewLessonFirstCallback.UpdateStartTime -> updateStartTime(
                                    state,
                                    callback.startTime
                                )

                                is NewLessonFirstCallback.UpdateSubjectText -> updateSubjectText(
                                    state,
                                    callback.subjectText
                                )

                                is NewLessonFirstCallback.UpdateTopicText -> updateTopic(
                                    state,
                                    callback.topic
                                )

                                is NewLessonFirstCallback.UpdateShowDatePicker -> updateDatePickerState(
                                    state,
                                    callback.datePickerEnabled
                                )

                                is NewLessonFirstCallback.UpdateShowTimePickerEnd -> updateTimePickerEndState(
                                    state,
                                    callback.timePickerEndEnabled
                                )

                                is NewLessonFirstCallback.UpdateShowTimePickerStart -> updateTimePickerStartState(
                                    state,
                                    callback.timePickerStartEnabled
                                )

                                is NewLessonFirstCallback.UpdateShowDropDownMenu -> updateDropDownMenuState(
                                    state,
                                    callback.dropDownMenuEnabled
                                )

                                is NewLessonFirstCallback.UpdateShowTimePickerStartTextField -> updateTimePickerStartTextFieldState(
                                    state,
                                    callback.timePickerStartTextFieldEnabled
                                )

                                is NewLessonFirstCallback.UpdateShowTimePickerEndTextField -> updateTimePickerEndTextFieldState(
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
        state: NewLessonFirstUIState.Success,
        date: LocalDate
    ): NewLessonFirstUIState {
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
        state: NewLessonFirstUIState.Success,
        endTime: LocalDateTime
    ): NewLessonFirstUIState {
        return if (checkEndTime(endTime = endTime, startTime = state.state.startTime)) {
            state.copy(state = state.state.copy(endTime = endTime, showTimePickerEnd = false))
        } else {
            state
        }
    }

    private suspend fun updateStartTime(
        state: NewLessonFirstUIState.Success,
        startTime: LocalDateTime
    ): NewLessonFirstUIState {
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
        state: NewLessonFirstUIState.Success,
        subjectText: String
    ): NewLessonFirstUIState {
        val filteredSubjects = newLessonRepository.getSubjects(subjectText)
        return state.copy(
            state = state.state.copy(
                subjectText = subjectText,
                loadedSubjects = filteredSubjects
            )
        )
    }

    private fun updateTopic(
        state: NewLessonFirstUIState.Success,
        topic: String
    ): NewLessonFirstUIState {
        return state.copy(
            state = state.state.copy(
                topic = topic
            )
        )
    }

    private fun updateDatePickerState(
        state: NewLessonFirstUIState.Success,
        datePickerEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showDatePicker = datePickerEnabled))
    }

    private fun updateTimePickerStartState(
        state: NewLessonFirstUIState.Success,
        timePickerStartEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showTimePickerStart = timePickerStartEnabled))
    }

    private fun updateTimePickerEndState(
        state: NewLessonFirstUIState.Success,
        timePickerEndEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showTimePickerEnd = timePickerEndEnabled))
    }

    private fun updateDropDownMenuState(
        state: NewLessonFirstUIState.Success,
        dropDownMenuEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showDropdownMenu = dropDownMenuEnabled))
    }

    private fun updateTimePickerStartTextFieldState(
        state: NewLessonFirstUIState.Success,
        timePickerStartTextFieldEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showTimePickerStartTextField = timePickerStartTextFieldEnabled))
    }

    private fun updateTimePickerEndTextFieldState(
        state: NewLessonFirstUIState.Success,
        timePickerEndTextFieldEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showTimePickerEndTextField = timePickerEndTextFieldEnabled))
    }


}

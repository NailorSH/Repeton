package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonFirstScreenData
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem
import com.nailorsh.repeton.features.newlesson.data.repository.NewLessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

sealed interface NewLessonFirstAction {

    object NavigateBack : NewLessonFirstAction
    object SaveData : NewLessonFirstAction
    data class AddUser(val user: NewLessonUserItem) : NewLessonFirstAction
    data class RemoveUser(val user: NewLessonUserItem) : NewLessonFirstAction
    data class UpdateSubjectText(val subjectText: String) : NewLessonFirstAction
    data class UpdateTopicText(val topic: String) : NewLessonFirstAction
    data class UpdateDate(val date: LocalDate) : NewLessonFirstAction
    data class UpdateStartTime(val startTime: LocalDateTime) : NewLessonFirstAction
    data class UpdateEndTime(val endTime: LocalDateTime) : NewLessonFirstAction
    data class UpdateShowDatePicker(val datePickerEnabled: Boolean) : NewLessonFirstAction
    data class UpdateShowTimePickerStart(val timePickerStartEnabled: Boolean) : NewLessonFirstAction
    data class UpdateShowTimePickerEnd(val timePickerEndEnabled: Boolean) : NewLessonFirstAction

    data class UpdateShowDropDownMenu(val dropDownMenuEnabled: Boolean) : NewLessonFirstAction

    data class UpdateShowTimePickerStartTextField(val timePickerStartTextFieldEnabled: Boolean) :
        NewLessonFirstAction

    data class UpdateShowTimePickerEndTextField(val timePickerEndTextFieldEnabled: Boolean) :
        NewLessonFirstAction

    data class UpdateShowAddUserDialogue(val addUserDialogueEnable: Boolean) : NewLessonFirstAction

}

sealed class NewLessonUIEvent(@StringRes val errorMsg: Int) {

    object SubjectError : NewLessonUIEvent(R.string.new_lesson_screen_subject_error)

    object TopicError : NewLessonUIEvent(R.string.new_lesson_screen_topic_error)

    object DateError : NewLessonUIEvent(R.string.new_lesson_screen_date_error)

    object StartTimeError : NewLessonUIEvent(R.string.new_lesson_screen_start_time_error)

    object EndTimeError : NewLessonUIEvent(R.string.new_lesson_screen_end_time_error)

    object NoStudentsError : NewLessonUIEvent(R.string.new_lesson_screen_error_no_students)

    object StudentAlreadyInListError :
        NewLessonUIEvent(R.string.new_lesson_screen_error_student_already_in_list)

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

    val allStudents: List<NewLessonUserItem> = emptyList(),
    val pickedStudents: List<NewLessonUserItem> = emptyList(),

    val showDropdownMenu: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePickerStart: Boolean = false,
    val showTimePickerEnd: Boolean = false,
    val showAddUserDialogue: Boolean = false,

    val showTimePickerStartTextField: Boolean = false,
    val showTimePickerEndTextField: Boolean = false,

    )

@HiltViewModel
class NewLessonFirstViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewLessonFirstUIState>(NewLessonFirstUIState.Loading)
    val state: StateFlow<NewLessonFirstUIState> = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<NewLessonUIEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<NewLessonFirstNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private var subject: Subject? = null

    init {
        clearData()
    }

    private fun clearData() {
        viewModelScope.launch {
            _state.value = NewLessonFirstUIState.Loading

            withContext(Dispatchers.IO) {
                try {
                    val subjects = getFilteredSubjects("")
                    val students = newLessonRepository.getStudents()
                    _state.update {
                        NewLessonFirstUIState.Success(
                            NewLessonFirstState(
                                loadedSubjects = subjects,
                                allStudents = students
                            )
                        )
                    }
                } catch (e: IOException) {
                    /* TODO Обработать ошибку */
                } catch (e: HttpRetryException) {
                    /* TODO Обработать ошибку */
                }
            }

        }

    }

    private suspend fun getFilteredSubjects(subjectText: String) = withContext(Dispatchers.IO) {
        newLessonRepository.getSubjects(subjectText)
    }

    private suspend fun getSubject(subjectText: String) = withContext(Dispatchers.IO) {
        subject = newLessonRepository.getSubject(subjectText)
    }


    private suspend fun onSave() {
        if (_state.value is NewLessonFirstUIState.Success) {
            val state = (_state.value as NewLessonFirstUIState.Success).state
            if (!checkStudents(state.pickedStudents)) return
            if (!checkTopic(state.topic)) return
            getSubject(state.subjectText)
            if (!checkSubject()) return
            if (checkDate(state.startTime.toLocalDate()) && checkStartTime(state.startTime) && checkEndTime(
                    startTime = state.startTime,
                    endTime = state.endTime
                )
            ) {
                _navigationEvents.emit(
                    NewLessonFirstNavigationEvent.NavigateToNext(
                        NewLessonFirstScreenData(
                            subject = subject!!,
                            topic = state.topic,
                            startTime = state.startTime,
                            endTime = state.endTime,
                            students =  state.pickedStudents
                        )
                    )
                )
            }
        }

    }

    private suspend fun checkStudents(students: List<NewLessonUserItem>) : Boolean {
        return if (students.isNotEmpty()) {
            true
        } else {
            _uiEvents.emit(NewLessonUIEvent.NoStudentsError)
            false
        }
    }

    private suspend fun checkTopic(topic: String): Boolean {
        return if (topic.isNotBlank()) {
            true
        } else {
            _uiEvents.emit(NewLessonUIEvent.TopicError)
            false
        }
    }

    private suspend fun checkSubject(): Boolean {
        return if (subject != null) {
            true
        } else {
            _uiEvents.emit(NewLessonUIEvent.SubjectError)
            false
        }
    }

    private suspend fun checkStartTime(startTime: LocalDateTime): Boolean {
        return if (startTime >= LocalDateTime.now()) {
            true
        } else {
            _uiEvents.emit(NewLessonUIEvent.StartTimeError)
            false
        }
    }

    private suspend fun checkEndTime(startTime: LocalDateTime, endTime: LocalDateTime): Boolean {
        return if (startTime < endTime) {
            true
        } else {
            _uiEvents.emit(NewLessonUIEvent.EndTimeError)
            false
        }
    }

    private suspend fun checkDate(date: LocalDate): Boolean {
        return if (date >= LocalDate.now()) {
            true
        } else {
            _uiEvents.emit(NewLessonUIEvent.DateError)
            false
        }
    }

    fun onAction(action: NewLessonFirstAction) {
        viewModelScope.launch {
            when (action) {
                is NewLessonFirstAction.SaveData -> {
                    onSave()
                }

                is NewLessonFirstAction.NavigateBack -> {
                    _navigationEvents.emit(NewLessonFirstNavigationEvent.NavigateBack)
                }


                else -> {
                    _state.update {
                        when (val state = it) {
                            is NewLessonFirstUIState.Success -> {
                                when (action) {
                                    is NewLessonFirstAction.RemoveUser -> removeUser(
                                        state,
                                        action.user
                                    )

                                    is NewLessonFirstAction.AddUser -> addUser(
                                        state,
                                        action.user
                                    )

                                    is NewLessonFirstAction.UpdateDate -> updateDate(
                                        state,
                                        action.date
                                    )

                                    is NewLessonFirstAction.UpdateEndTime -> updateEndTime(
                                        state,
                                        action.endTime
                                    )

                                    is NewLessonFirstAction.UpdateStartTime -> updateStartTime(
                                        state,
                                        action.startTime
                                    )

                                    is NewLessonFirstAction.UpdateSubjectText -> updateSubjectText(
                                        state,
                                        action.subjectText
                                    )

                                    is NewLessonFirstAction.UpdateTopicText -> updateTopic(
                                        state,
                                        action.topic
                                    )

                                    is NewLessonFirstAction.UpdateShowDatePicker -> updateDatePickerState(
                                        state,
                                        action.datePickerEnabled
                                    )

                                    is NewLessonFirstAction.UpdateShowTimePickerEnd -> updateTimePickerEndState(
                                        state,
                                        action.timePickerEndEnabled
                                    )

                                    is NewLessonFirstAction.UpdateShowTimePickerStart -> updateTimePickerStartState(
                                        state,
                                        action.timePickerStartEnabled
                                    )

                                    is NewLessonFirstAction.UpdateShowDropDownMenu -> updateDropDownMenuState(
                                        state,
                                        action.dropDownMenuEnabled
                                    )

                                    is NewLessonFirstAction.UpdateShowTimePickerStartTextField -> updateTimePickerStartTextFieldState(
                                        state,
                                        action.timePickerStartTextFieldEnabled
                                    )

                                    is NewLessonFirstAction.UpdateShowTimePickerEndTextField -> updateTimePickerEndTextFieldState(
                                        state,
                                        action.timePickerEndTextFieldEnabled
                                    )

                                    is NewLessonFirstAction.UpdateShowAddUserDialogue -> updateAddUserDialogueState(
                                        state,
                                        action.addUserDialogueEnable
                                    )

                                    else -> state
                                }
                            }

                            else -> it
                        }
                    }
                    if (action is NewLessonFirstAction.UpdateSubjectText) {
                        try {
                            val filteredSubjects = getFilteredSubjects(action.subjectText)
                            _state.update { currentState ->
                                if (currentState is NewLessonFirstUIState.Success) {
                                    currentState.copy(
                                        state = currentState.state.copy(
                                            loadedSubjects = filteredSubjects
                                        )
                                    )
                                } else {
                                    currentState
                                }
                            }
                        } catch (e: IOException) {
                            /* TODO Обработать ошибку */
                        } catch (e: HttpRetryException) {
                            /* TODO Обработать ошибку */
                        }

                    }
                }
            }
        }

    }

    private suspend fun addUser(
        state: NewLessonFirstUIState.Success,
        user: NewLessonUserItem
    ): NewLessonFirstUIState {
        return if (user !in state.state.pickedStudents) {
            state.copy(
                state = state.state.copy(
                    pickedStudents = state.state.pickedStudents.plus(user)
                )
            )
        } else {
            _uiEvents.emit(NewLessonUIEvent.StudentAlreadyInListError)
            state
        }

    }

    private suspend fun removeUser(
        state: NewLessonFirstUIState.Success,
        user: NewLessonUserItem
    ): NewLessonFirstUIState {
        return state.copy(
            state = state.state.copy(
                pickedStudents = state.state.pickedStudents.minusElement(user)
            )
        )
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
        return state.copy(
            state = state.state.copy(
                subjectText = subjectText,
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

    private fun updateAddUserDialogueState(
        state: NewLessonFirstUIState.Success,
        addUserDialogueEnabled: Boolean
    ): NewLessonFirstUIState {
        return state.copy(state = state.state.copy(showAddUserDialogue = addUserDialogueEnabled))
    }
}

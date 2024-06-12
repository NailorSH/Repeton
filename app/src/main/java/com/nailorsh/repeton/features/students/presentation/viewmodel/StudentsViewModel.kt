package com.nailorsh.repeton.features.students.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.user.User
import com.nailorsh.repeton.features.students.data.StudentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.HttpRetryException
import java.util.Locale
import javax.inject.Inject


sealed interface StudentsNavigationEvent {
    object NavigateBack : StudentsNavigationEvent

}

sealed class StudentsUIEvent(@StringRes val msg: Int) {
    object ErrorLoading : StudentsUIEvent(R.string.students_screen_loading_error)

    object TooMuchStudents : StudentsUIEvent(R.string.students_screen_too_much_students)

    object StudentAlreadyAdded : StudentsUIEvent(R.string.students_screen_student_already_added)

    object UpdateStudentError : StudentsUIEvent(R.string.students_screen_update_student_error)
}

sealed interface ManageStudent
sealed interface StudentsAction {
    object RetryAction : StudentsAction
    object NavigateBack : StudentsAction
    object Search : StudentsAction
    data class QueryChange(val query: String) : StudentsAction
    data class AddStudent(val student: User) : StudentsAction, ManageStudent
    data class RemoveStudent(val student: User) : StudentsAction, ManageStudent
    data class UpdateLoadingScreen(val isLoading: Boolean) : StudentsAction
    object ConfirmAlertDialog : StudentsAction
    object DismissAlertDialog : StudentsAction
}


data class StudentsState(
    val isLoading: Boolean = true,
    val error: Boolean = false,

    val studentsList: List<User> = emptyList(),
    val userStudents: List<User> = emptyList(),

    val query: String = "",
    val studentAction: ManageStudent? = null,
    val showLoadingScreen: Boolean = false
)

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val studentsRepository: StudentsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StudentsState())
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<StudentsUIEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<StudentsNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private lateinit var studentsList: List<User>

    init {
        getStudents()
    }

    private fun getStudents() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                withContext(Dispatchers.IO) {
                    studentsList = studentsRepository.getStudents()
                    val userStudents = studentsRepository.getUserStudents()
                    _state.update {
                        it.copy(
                            studentsList = studentsList,
                            userStudents = userStudents ?: emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiEvents.emit(StudentsUIEvent.ErrorLoading)
            }
            _state.update { it.copy(isLoading = false) }
        }
    }


    fun onAction(action: StudentsAction) {
        viewModelScope.launch {
            when (action) {
                StudentsAction.RetryAction -> {
                    getStudents()
                }

                is StudentsAction.UpdateLoadingScreen -> {
                    _state.update { state ->
                        state.copy(showLoadingScreen = action.isLoading)
                    }
                }


                StudentsAction.NavigateBack -> {
                    _navigationEvents.emit(StudentsNavigationEvent.NavigateBack)
                }

                is StudentsAction.QueryChange -> {
                    _state.update { state -> state.copy(query = action.query) }
                }


                StudentsAction.Search -> {
                    _state.update { state ->
                        state.copy(
                            studentsList = studentsList.filter {
                                it.name.lowercase(Locale.getDefault()).startsWith(
                                    prefix = state.query.lowercase(
                                        Locale.getDefault()
                                    )
                                )
                            }
                        )
                    }
                }

                is StudentsAction.AddStudent -> _state.update { state ->
                    state.copy(
                        studentAction = action
                    )
                }

                is StudentsAction.RemoveStudent -> _state.update { state ->
                    state.copy(
                        studentAction = action
                    )
                }

                is StudentsAction.ConfirmAlertDialog -> withContext(Dispatchers.IO) {
                    _state.update { state ->
                        state.copy(
                            isLoading = true
                        )
                    }
                    try {
                        when (val studentAction = _state.value.studentAction) {

                            is StudentsAction.AddStudent -> {
                                if (_state.value.userStudents.size >= 5) {
                                    _uiEvents.emit(StudentsUIEvent.TooMuchStudents)
                                } else if (!_state.value.userStudents.none { it.id == studentAction.student.id }) {
                                    _uiEvents.emit(StudentsUIEvent.StudentAlreadyAdded)
                                } else {
                                    studentsRepository.addStudent(studentAction.student.id)
                                    _state.update { state ->
                                        state.copy(
                                            userStudents = state.userStudents.plus(studentAction.student)
                                        )
                                    }
                                }
                            }

                            is StudentsAction.RemoveStudent -> {
                                if (_state.value.userStudents.contains(studentAction.student)) {
                                    studentsRepository.removeStudent(studentAction.student.id)
                                    _state.update { state ->
                                        state.copy(
                                            userStudents = state.userStudents.minusElement(
                                                studentAction.student
                                            )
                                        )
                                    }
                                }
                            }

                            null -> {
                            }
                        }
                    } catch (e: IOException) {
                        _uiEvents.emit(StudentsUIEvent.UpdateStudentError)
                    } catch (e: HttpRetryException) {
                        _uiEvents.emit(StudentsUIEvent.UpdateStudentError)
                    } catch (e: Exception) {
                        _uiEvents.emit(StudentsUIEvent.UpdateStudentError)
                    }

                    _state.update { state ->
                        state.copy(
                            studentAction = null,
                            isLoading = false
                        )
                    }

                }

                StudentsAction.DismissAlertDialog -> _state.update { state ->
                    state.copy(
                        studentAction = null
                    )
                }
            }
        }

    }


}
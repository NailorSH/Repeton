package com.nailorsh.repeton.features.subjects.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice
import com.nailorsh.repeton.features.subjects.data.SubjectsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject


sealed interface SubjectsNavigationEvent {
    object NavigateBack : SubjectsNavigationEvent

}

sealed class SubjectsUIEvent(@StringRes val msg: Int) {
    object ErrorLoading : SubjectsUIEvent(R.string.subject_screen_loading_error)

    object TooMuchSubject : SubjectsUIEvent(R.string.subjects_screen_too_much_subjects)

    object SubjectAlreadyAdded : SubjectsUIEvent(R.string.subjects_screen_subject_already_added)
}

sealed interface SubjectsAction {
    object RetryAction : SubjectsAction
    object NavigateBack : SubjectsAction
    object Search : SubjectsAction
    object SaveUpdates : SubjectsAction
    data class QueryChange(val query: String) : SubjectsAction
    data class AddSubject(val subject: Subject) : SubjectsAction

    data class RemoveSubject(val subject: SubjectWithPrice) : SubjectsAction

    data class SaveSubjectPrice(val subject: SubjectWithPrice, val price: String) : SubjectsAction

    data class UpdateLoadingScreen(val isLoading: Boolean) : SubjectsAction
}

data class SubjectsState(
    val isLoading: Boolean = true,
    val error: Boolean = false,

    val subjectsList: List<Subject> = emptyList(),
    val userSubjects: List<SubjectWithPrice> = emptyList(),

    val query: String = "",

    val showLoadingScreen: Boolean = false
)

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectsRepository: SubjectsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SubjectsState())
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<SubjectsUIEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<SubjectsNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private lateinit var subjectsList: List<Subject>

    init {
        getSubjects()
    }

    fun getSubjects() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                withContext(Dispatchers.IO) {
                    subjectsList = subjectsRepository.getSubjects()
                    val userSubjects = subjectsRepository.getUserSubjects()
                    _state.update {
                        it.copy(
                            subjectsList = subjectsList,
                            userSubjects = userSubjects ?: emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiEvents.emit(SubjectsUIEvent.ErrorLoading)
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun saveUpdates() = withContext(Dispatchers.IO) {
        subjectsRepository.updateUserSubjects(_state.value.userSubjects.ifEmpty { null })
    }

    fun onAction(action: SubjectsAction) {
        viewModelScope.launch {
            when (action) {
                SubjectsAction.RetryAction -> {
                    getSubjects()
                }

                SubjectsAction.SaveUpdates -> {
                    onAction(SubjectsAction.UpdateLoadingScreen(true))
                    saveUpdates()
                    onAction(SubjectsAction.UpdateLoadingScreen(false))
                    _navigationEvents.emit(SubjectsNavigationEvent.NavigateBack)
                }

                is SubjectsAction.UpdateLoadingScreen -> {
                    _state.update { state ->
                        state.copy(showLoadingScreen = action.isLoading)
                    }
                }

                is SubjectsAction.SaveSubjectPrice -> {
                    _state.update { state ->
                        state.copy(
                            userSubjects = state.userSubjects.map { subject ->
                                if (subject == action.subject) {
                                    subject.copy(price = if (action.price.isNotEmpty()) action.price.toInt() else 0)
                                } else {
                                    subject
                                }
                            }
                        )
                    }
                }

                is SubjectsAction.AddSubject -> {
                    if (_state.value.showLoadingScreen) return@launch
                    if (_state.value.userSubjects.size >= 5) {
                        _uiEvents.emit(SubjectsUIEvent.TooMuchSubject)
                    } else if (_state.value.userSubjects.filter { subject ->
                            subject.subject == action.subject
                        }.isNotEmpty()) {
                        _uiEvents.emit(SubjectsUIEvent.SubjectAlreadyAdded)
                    } else {
                        _state.update { state ->
                            state.copy(
                                userSubjects = state.userSubjects.plus(
                                    SubjectWithPrice(
                                        price = 0,
                                        subject = action.subject
                                    )
                                )
                            )
                        }
                    }

                }

                SubjectsAction.NavigateBack -> {
                    _navigationEvents.emit(SubjectsNavigationEvent.NavigateBack)
                }

                is SubjectsAction.QueryChange -> {
                    _state.update { state -> state.copy(query = action.query) }
                }

                is SubjectsAction.RemoveSubject -> {
                    _state.update { state ->
                        state.copy(userSubjects = state.userSubjects.minusElement(action.subject))
                    }
                }

                SubjectsAction.Search -> {
                    _state.update { state ->
                        state.copy(
                            subjectsList = subjectsList.filter {
                                it.name.lowercase(Locale.getDefault()).startsWith(
                                    prefix = state.query.lowercase(
                                        Locale.getDefault()
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

    }


}
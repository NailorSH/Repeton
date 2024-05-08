package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Homework
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import java.time.LocalDateTime
import javax.inject.Inject


sealed interface NewLessonUiState {
    data class Success(val subjects: List<Subject>) : NewLessonUiState

    data class ErrorSaving(val error: Boolean, val subjects: List<Subject>) : NewLessonUiState

    object SuccessSaving : NewLessonUiState
    object Error : NewLessonUiState
    object Loading : NewLessonUiState

}

enum class NewLessonSecondUiState {
    None, Saved
}

data class NewLessonState(
    val uiState: NewLessonUiState = NewLessonUiState.Loading,
    val secondUiState: NewLessonSecondUiState = NewLessonSecondUiState.None,
    val subject: Subject = Subject("-1", ""),
    val topic: String = "",
    val startTime: LocalDateTime = LocalDateTime.now().plusMinutes(1),
    val endTime: LocalDateTime = LocalDateTime.now().plusMinutes(30),
    val description: String? = null,
    val additionalMaterials: String? = null,
    val homework: Homework? = null
)

@HiltViewModel
class NewLessonViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewLessonState())
    val state: StateFlow<NewLessonState> = _state.asStateFlow()

    private val _filteredSubjects = MutableStateFlow<List<Subject>>(emptyList())
    val filteredSubjects: StateFlow<List<Subject>> = _filteredSubjects.asStateFlow()

    init {
        clearData()
    }

    fun getSubjects() {
        viewModelScope.launch {
            _state.update { it.copy(uiState = NewLessonUiState.Loading, secondUiState = NewLessonSecondUiState.None) }
            try {
                val subjects = newLessonRepository.getSubjects()
                _state.update { currentState ->
                    currentState.copy(uiState = NewLessonUiState.Success(subjects))
                }
            } catch (e: IOException) {
                _state.update { it.copy(uiState = NewLessonUiState.Error) }
            } catch (e: HttpRetryException) {
                _state.update { it.copy(uiState = NewLessonUiState.Error) }
            }
        }
    }

    fun saveLesson(description: String?, homework: Homework?, additionalMaterials: String?) {
        viewModelScope.launch {
            newLessonRepository.saveNewLesson(
                Lesson(
                    id = Id("0"),
                    subject = _state.value.subject,
                    topic = _state.value.topic,
                    startTime = _state.value.startTime,
                    endTime = _state.value.endTime,
                    tutor = FakeTutorsSource.getTutorById(Id("1"))!!,
                    description = description,
                    homework = homework,
                    additionalMaterials = additionalMaterials
                )
            )
            _state.update { it.copy(secondUiState = NewLessonSecondUiState.Saved) }
        }
    }

    fun clearData() {
        getSubjects()
        _state.update {
            it.copy(
                subject = Subject("-1", ""),
                topic = "",
                startTime = LocalDateTime.now().plusMinutes(1),
                endTime = LocalDateTime.now().plusMinutes(30),
                description = null,
                homework = null,
                additionalMaterials = null
            )
        }
    }

    fun updateFilteredSubjects(filter: String) {
        viewModelScope.launch {
            val allSubjects = when (val uiState = _state.value.uiState) {
                is NewLessonUiState.Success -> uiState.subjects
                is NewLessonUiState.ErrorSaving -> uiState.subjects
                else -> emptyList()
            }

            _filteredSubjects.value = allSubjects.filter { subject ->
                subject.name.lowercase().startsWith(filter.lowercase())
            }
        }
    }

    fun saveRequiredFields(subject: String, title: String, startTime: LocalDateTime, endTime: LocalDateTime) {
        viewModelScope.launch {
            val resultSubject = newLessonRepository.getSubject(subject) ?: Subject("-1", "")
            val error = resultSubject.id == "-1"

            if (error) {
                _state.update {
                    it.copy(
                        uiState = NewLessonUiState.ErrorSaving(
                            true,
                            _state.value.uiState.let { uiState ->
                                when (uiState) {
                                    is NewLessonUiState.ErrorSaving ->
                                        uiState.subjects.ifEmpty { listOf() }

                                    is NewLessonUiState.Success -> uiState.subjects
                                    else -> listOf()
                                }
                            })

                    )
                }
            } else {
                _state.update {
                    it.copy(
                        subject = resultSubject,
                        topic = title,
                        startTime = startTime,
                        endTime = endTime,
                        uiState = NewLessonUiState.SuccessSaving
                    )
                }
            }
        }
    }
}

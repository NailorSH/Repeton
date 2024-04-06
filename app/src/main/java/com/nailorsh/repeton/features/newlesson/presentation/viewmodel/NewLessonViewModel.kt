package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.common.data.models.Homework
import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.common.data.models.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
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

sealed interface NewLessonSecondUiState {
    object None : NewLessonSecondUiState

    object Saved : NewLessonSecondUiState
}

@HiltViewModel
class NewLessonViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {
    var newLessonUiState: NewLessonUiState by mutableStateOf(NewLessonUiState.Loading)
        private set

    var newLessonSecondUiState : NewLessonSecondUiState by mutableStateOf(NewLessonSecondUiState.None)
        private set

    var _subject = Subject(-1, "")
        private set
    var _topic = ""
        private set
    var _startTime = LocalDateTime.now().plusMinutes(1)
        private set
    var _endTime = LocalDateTime.now().plusMinutes(30)
        private set

    private var _description : String? = null
    private var _additionalMaterials : String? = null
    private var _homework : Homework? = null
    private lateinit var _subjects : List<Subject>

    init {
        clearData()
    }

    fun getSubjects() {
        viewModelScope.launch {
            newLessonUiState = NewLessonUiState.Loading
            newLessonSecondUiState = NewLessonSecondUiState.None
            newLessonUiState = try {
                /* TODO Добавить репозиторий для NewLesson и метод получения всех доступных предметов */
                _subjects = newLessonRepository.getSubjects()
                NewLessonUiState.Success(_subjects)
            } catch (e: IOException) {
                NewLessonUiState.Error
            } catch (e: HttpRetryException) {
                NewLessonUiState.Error
            }
        }
    }

    fun saveLesson(description : String?, homework: Homework?, additionalMaterials : String?) {
        viewModelScope.launch {
            _description = description
            _homework = homework
            _additionalMaterials = additionalMaterials
            newLessonRepository.saveNewLesson(Lesson(
                subject = _subject,
                topic = _topic,
                startTime = _startTime,
                endTime = _endTime,
                teacherName = "Placeholder",
                description = _description,
                homework = _homework,
                additionalMaterials = _additionalMaterials
            ))
            newLessonSecondUiState = NewLessonSecondUiState.Saved
        }
    }

    fun clearData() {
        getSubjects()
        _subject = Subject(-1, "")
        _topic = ""
        _startTime = LocalDateTime.now().plusMinutes(1)
        _endTime = LocalDateTime.now().plusMinutes(30)
        _description = null
        _homework = null
        _additionalMaterials = null
    }

    fun saveRequiredFields(subject: String, title: String, startTime: LocalDateTime, endTime: LocalDateTime) {
        viewModelScope.launch {
            var error = false

            val resultSubject = newLessonRepository.getSubject(subject)
            if (resultSubject == null) {
                error = true
                _subject = Subject(-1, "")
            }

            if (error) {
                newLessonUiState = NewLessonUiState.ErrorSaving(error, _subjects)
            } else {
                _subject = resultSubject!!
                _topic = title
                _startTime = startTime
                _endTime = endTime

                newLessonUiState = NewLessonUiState.SuccessSaving
            }
        }
    }
}
package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.play.integrity.internal.i
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.common.data.models.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


sealed interface NewLessonUiState {
    data class Success(val subjects: List<Subject>) : NewLessonUiState

    data class ErrorSaving(val errors: List<Int>) : NewLessonUiState
    object Error : NewLessonUiState
    object Loading : NewLessonUiState

}

@HiltViewModel
class NewLessonViewModel @Inject constructor(
    private val newLessonRepository: NewLessonRepository
) : ViewModel() {
    var newLessonUiState: NewLessonUiState by mutableStateOf(NewLessonUiState.Loading)
        private set

    private lateinit var _lesson: Lesson
    private lateinit var _subjects: List<Subject>

    init {
        getSubjects()
    }

    companion object {
        const val ERROR_FOR_SUBJECT_TEXT_FIELD = 0
        const val ERROR_FOR_TOPIC_TEXT_FIELD = 1
        const val ERROR_FOR_DATE_TEXT_FIELD = 2
        const val ERROR_FOR_START_TIME_TEXT_FIELD = 3
        const val ERROR_FOR_END_TIME_TEXT_FIELD = 4
    }

    fun getSubjects() {
        viewModelScope.launch {
            newLessonUiState = NewLessonUiState.Loading
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

    fun saveNewLesson(lesson: Lesson) {
        viewModelScope.launch {
            newLessonRepository.saveNewLesson(lesson)
        }
    }

    fun saveRequiredFields(subject: String, title: String, startTime: LocalDateTime, endTime: LocalDateTime) : Boolean {

        val errorsList = MutableList(5) { 0 }
        var error: Boolean = false
        var resultSubject: Subject = Subject(0, "")

        if (this::_subjects.isInitialized) {
            val sub = _subjects.firstOrNull { it.subjectName == subject }
            if (sub == null) {
                errorsList.add(ERROR_FOR_SUBJECT_TEXT_FIELD, 1)
                error = true
            } else {
                resultSubject = sub
                errorsList.add(ERROR_FOR_SUBJECT_TEXT_FIELD, 0)
            }
        }

        if (startTime.toLocalDate() < LocalDate.now()) {
            error = true
            errorsList.add(ERROR_FOR_DATE_TEXT_FIELD, 1)
            errorsList.add(ERROR_FOR_START_TIME_TEXT_FIELD, 0)
            errorsList.add(ERROR_FOR_END_TIME_TEXT_FIELD, 0)
        } else {
            errorsList.add(ERROR_FOR_DATE_TEXT_FIELD, 0)
            if (startTime < LocalDateTime.now()) {
                error = true
                errorsList.add(ERROR_FOR_START_TIME_TEXT_FIELD, 1)
                errorsList.add(ERROR_FOR_END_TIME_TEXT_FIELD, 0)
            } else {
                errorsList.add(ERROR_FOR_START_TIME_TEXT_FIELD, 0)
                if (endTime <= startTime) {
                    error = true
                    errorsList.add(ERROR_FOR_END_TIME_TEXT_FIELD, 1)
                } else {
                    errorsList.add(ERROR_FOR_END_TIME_TEXT_FIELD, 0)
                }
            }
        }

        if (error) {
            newLessonUiState = NewLessonUiState.ErrorSaving(errorsList)
        } else {
            _lesson = Lesson(
                subject = resultSubject,
                title = title,
                startTime = startTime,
                endTime = endTime,
                teacherName = "Placeholder"
            )
        }





        return error
    }

}
package com.nailorsh.repeton.schedule.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.currentlesson.data.model.Lesson
import com.nailorsh.repeton.schedule.data.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface ScheduleUiState {
    data class Success(val lessons: Map<LocalDate, List<Lesson>>) : ScheduleUiState
    object Error : ScheduleUiState
    object Loading : ScheduleUiState
}

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    var scheduleUiState: ScheduleUiState by mutableStateOf(ScheduleUiState.Loading)
        private set

    private var lessonsCache = mutableMapOf<LocalDate, MutableList<Lesson>>()


    init {
        getLessons()
    }

    fun getLessons() {
        viewModelScope.launch {
            scheduleUiState = ScheduleUiState.Loading
            try {
                val lessons = scheduleRepository.getLessons()
                lessons.forEach { lesson ->
                    val day = LocalDate.from(lesson.startTime)
                    lessonsCache[day] = (lessonsCache[day] ?: mutableListOf()).also {
                        it.add(lesson)
                    }
                }
                val todayLessons = lessonsCache ?: emptyMap<LocalDate, MutableList<Lesson>>()
                scheduleUiState = ScheduleUiState.Success(todayLessons)
            } catch (e: Exception) {
                scheduleUiState = ScheduleUiState.Error
            }
        }
    }

}
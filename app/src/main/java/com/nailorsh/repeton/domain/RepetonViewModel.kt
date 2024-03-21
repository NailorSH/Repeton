package com.nailorsh.repeton.domain

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.domain.repositories.RepetonRepository
import com.nailorsh.repeton.model.Chat
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.model.Tutor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import java.time.LocalDate
import javax.inject.Inject

sealed interface SearchUiState {
    data class Success(val tutors: List<Tutor>) : SearchUiState
    object Error : SearchUiState
    object Loading : SearchUiState
    object None : SearchUiState
}


sealed interface CurrentLessonUiState {
    data class Success(val lesson: Lesson) : CurrentLessonUiState
    object Error : CurrentLessonUiState
    object Loading : CurrentLessonUiState
}

sealed interface ScheduleUiState {
    data class Success(val lessons: Map<LocalDate, List<Lesson>>) : ScheduleUiState
    object Error : ScheduleUiState
    object Loading : ScheduleUiState
}

sealed interface ChatsUiState {
    data class Success(val chats: List<Chat>) : ChatsUiState
    object Error : ChatsUiState
    object Loading : ChatsUiState
}

@HiltViewModel
class RepetonViewModel @Inject constructor(
    private val repetonRepository: RepetonRepository
) : ViewModel() {
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.None)
        private set

    var scheduleUiState: ScheduleUiState by mutableStateOf(ScheduleUiState.Loading)
        private set

    var chatsUiState: ChatsUiState by mutableStateOf(ChatsUiState.Loading)
        private set

    var currentLessonUiState: CurrentLessonUiState by mutableStateOf(CurrentLessonUiState.Loading)
        private set

    private var lessonsCache = mutableMapOf<LocalDate, MutableList<Lesson>>()


    init {
        getLessons()
    }

    fun getLessons() {
        viewModelScope.launch {
            scheduleUiState = ScheduleUiState.Loading
            try {
                val lessons = repetonRepository.getLessons()
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
//
//    fun getLessons(day: LocalDate) {
//        viewModelScope.launch {
//            scheduleUiState = ScheduleUiState.Loading
//            scheduleUiState = try {
//                ScheduleUiState.Success(lessonsCache.getOrDefault(day, listOf()))
//            } catch (e: IOException) {
//                ScheduleUiState.Error
//            } catch (e: HttpRetryException) {
//                ScheduleUiState.Error
//            }
//
//        }
//    }


    fun getLesson(lessonId: Int) {
        viewModelScope.launch {
            currentLessonUiState = CurrentLessonUiState.Loading
            currentLessonUiState = try {
                val lesson = repetonRepository.getLesson(lessonId)
                CurrentLessonUiState.Success(lesson)
            } catch (e: IOException) {
                CurrentLessonUiState.Error
            } catch (e: HttpRetryException) {
                CurrentLessonUiState.Error
            }
        }
    }

    private var tutorSearchJob: Job? = null

    fun typingTutorSearch(prefix: String) {
        with(prefix) {
            when {
                length > 1 -> {
                    tutorSearchJob?.cancel()
                    tutorSearchJob = viewModelScope.launch {
                        delay(600) //debounce
                        searchUiState = SearchUiState.Loading
                        delay(2000)
                        searchUiState = try {
                            SearchUiState.Success(repetonRepository.getTutors())
                        } catch (e: IOException) {
                            SearchUiState.Error
                        } catch (e: HttpRetryException) {
                            SearchUiState.Error
                        }
                    }
                }
            }
        }
    }

    fun getTutors() {
        viewModelScope.launch {
            searchUiState = SearchUiState.Loading
            delay(2000)
            searchUiState = try {
                SearchUiState.Success(repetonRepository.getTutors())
            } catch (e: IOException) {
                SearchUiState.Error
            } catch (e: HttpRetryException) {
                SearchUiState.Error
            }
        }
    }

    fun getChats() {
        viewModelScope.launch {
            chatsUiState = ChatsUiState.Loading
            delay(2000)
            chatsUiState = try {
                ChatsUiState.Success(repetonRepository.getChats())
            } catch (e: IOException) {
                ChatsUiState.Error
            } catch (e: HttpRetryException) {
                ChatsUiState.Error
            }
        }
    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application =
//                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RepetonApplication)
//                val repetonRepository = application.container.repetonRepository
//                RepetonViewModel(repetonRepository = repetonRepository)
//            }
//        }
//    }
}
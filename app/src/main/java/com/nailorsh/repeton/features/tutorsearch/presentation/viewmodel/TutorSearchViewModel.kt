package com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.features.tutorsearch.data.TutorSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface TutorSearchNavigationEvent {
    data class NavigateToTutorProfile(val tutor: Tutor) : TutorSearchNavigationEvent
    object NavigateBack : TutorSearchNavigationEvent
}

sealed class TutorSearchUIEvent(@StringRes val msg: Int) {
    object ErrorLoading : TutorSearchUIEvent(R.string.tutor_search_screen_loading_error)
}

sealed interface TutorSearchAction {
    object RetryAction : TutorSearchAction
    object NavigateBack : TutorSearchAction
    data class NavigateToTutorProfile(val tutor: Tutor) : TutorSearchAction
    object Search : TutorSearchAction
    data class ActiveChange(val active: Boolean) : TutorSearchAction
    object Clear : TutorSearchAction
    data class QueryChange(val query: String) : TutorSearchAction
    data class UpdateLoadingScreen(val isLoading: Boolean) : TutorSearchAction
}

data class TutorSearchState(
    val isLoading: Boolean = true,
    val error: Boolean = false,

    val tutorsList: List<Tutor> = emptyList(),

    val query: String = "",
    val active: Boolean = false,
    val showLoadingScreen: Boolean = false
)

@HiltViewModel
class TutorSearchViewModel @Inject constructor(
    private val tutorSearchRepository: TutorSearchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TutorSearchState())
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<TutorSearchUIEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<TutorSearchNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private lateinit var tutorsList: List<Tutor>

    init {
        getTutors()
    }

    private fun getTutors() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                withContext(Dispatchers.IO) {
                    tutorsList = tutorSearchRepository.getTutors()
                    _state.update {
                        it.copy(
                            tutorsList = tutorsList,
                        )
                    }
                }
            } catch (e: IOException) {
                _uiEvents.emit(TutorSearchUIEvent.ErrorLoading)
            } catch (e: HttpRetryException) {
                _uiEvents.emit(TutorSearchUIEvent.ErrorLoading)
            } catch (e: NoSuchElementException) {
                _uiEvents.emit(TutorSearchUIEvent.ErrorLoading)
            } catch (e: Exception) {
                _uiEvents.emit(TutorSearchUIEvent.ErrorLoading)
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onAction(action: TutorSearchAction) {
        viewModelScope.launch {
            when (action) {
                is TutorSearchAction.RetryAction -> {
                    getTutors()
                }

                is TutorSearchAction.UpdateLoadingScreen -> {
                    _state.update { state ->
                        state.copy(showLoadingScreen = action.isLoading)
                    }
                }

                is TutorSearchAction.NavigateBack -> {
                    _navigationEvents.emit(TutorSearchNavigationEvent.NavigateBack)
                }

                is TutorSearchAction.NavigateToTutorProfile -> {
                    _navigationEvents.emit(
                        TutorSearchNavigationEvent.NavigateToTutorProfile(
                            action.tutor
                        )
                    )
                }

                is TutorSearchAction.QueryChange -> {
                    _state.update { state -> state.copy(query = action.query) }
                }

                is TutorSearchAction.Search -> {
                    _state.update { state ->
                        state.copy(
                            tutorsList = tutorsList.filter {
                                it.doesMatchSearchQuery(state.query)
                            },
                            active = false
                        )
                    }
                }

                is TutorSearchAction.Clear -> {
                    _state.update { state ->
                        if (state.query.isNotEmpty()) state.copy(query = "")
                        else state.copy(active = false)
                    }
                }

                is TutorSearchAction.ActiveChange -> {
                    _state.update { state -> state.copy(active = action.active) }
                }
            }
        }
    }
}
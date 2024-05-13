package com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.features.tutorsearch.data.TutorSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface SearchUiState {
    data class Success(val tutors: List<Tutor>) : SearchUiState
    object Error : SearchUiState
    object Loading : SearchUiState
    object None : SearchUiState
}

@HiltViewModel
class TutorSearchViewModel @Inject constructor(
    private val tutorSearchRepository: TutorSearchRepository
) : ViewModel() {
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.None)
        private set

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
                            withContext(Dispatchers.IO) {
                                SearchUiState.Success(tutorSearchRepository.getTutors())
                            }
                        } catch (e: IOException) {
                            SearchUiState.Error
                        } catch (e: HttpRetryException) {
                            SearchUiState.Error
                        } catch (e: NoSuchElementException) {
                            SearchUiState.Error
                        } catch (e: Exception) {
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
            searchUiState = try {
                withContext(Dispatchers.IO) {
                    SearchUiState.Success(tutorSearchRepository.getTutors())
                }
            } catch (e: IOException) {
                SearchUiState.Error
            } catch (e: HttpRetryException) {
                SearchUiState.Error
            } catch (e: NoSuchElementException) {
                SearchUiState.Error
            } catch (e: Exception) {
                SearchUiState.Error
            }
        }
    }
}
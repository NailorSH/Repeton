package com.nailorsh.repeton.features.newlesson.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonFirstScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class NewLessonSharedViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NewLessonFirstScreenData())
    val state = _state.asStateFlow()

    fun updateState(firstScreenData: NewLessonFirstScreenData) {
        _state.value = firstScreenData
    }

}
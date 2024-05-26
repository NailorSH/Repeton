package com.nailorsh.repeton.features.about.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.features.about.data.AboutRepository
import com.nailorsh.repeton.features.about.data.mappers.toDomain
import com.nailorsh.repeton.features.about.data.model.EducationItem
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
import javax.inject.Inject

data class AboutState(
    val isLoading: Boolean = true,
    val error: Boolean = false,

    val photoSrc: String? = null,
    val name: String = "",
    val surname: String = "",
    val isTutor: Boolean = false,

    val about: String = "",
    val aboutNew: String = "",

    val education: Education? = null,
    val languages: List<Language>? = null,

    val educationList : List<EducationItem> = emptyList(),

    val changeOptionsIsExpanded: Boolean = false,
    val educationListIsExpanded: Boolean = false,
    val aboutIsChanging: Boolean = false,

    )

sealed interface AboutAction {
    object NavigateBack : AboutAction
    data class ChangeAbout(val about: String) : AboutAction
    data class ChangeSpecialization(val spec: String) : AboutAction
    data class ChangeEducation(val education: EducationItem) : AboutAction
    object SaveAbout : AboutAction
    object SaveAboutConfirmed : AboutAction
    object DismissAbout : AboutAction
    object DismissAboutConfirmed : AboutAction
    object RetryAction : AboutAction
    object RefreshAction : AboutAction
    object UpdateShowChangePhoto : AboutAction

    data class UpdateShowChangeOptions(val isExpanded: Boolean) : AboutAction

    data class UpdateShowChangeAbout(val isChanging: Boolean) : AboutAction

    data class UpdateShowEducationList(val isExpanded: Boolean) : AboutAction

}

sealed class AboutUiEvent(@StringRes val msg: Int) {

}

sealed interface AboutNavigationEvent {
    object NavigateBack : AboutNavigationEvent
}

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val aboutRepository: AboutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AboutState())
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<AboutUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _navigationEvents = MutableSharedFlow<AboutNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()


    private fun getData() {
        viewModelScope.launch {
            _state.value = AboutState()
            try {
                withContext(Dispatchers.IO) {
                    val userData = aboutRepository.getUserData()
                    _state.update { state ->
                        state.copy(
                            photoSrc = userData.photoSrc,
                            name = userData.name,
                            surname = userData.surname,
                            about = userData.about ?: "",
                            aboutNew = userData.about ?: "",
                            education = userData.education,
                            languages = userData.languages,
                            isTutor = userData.isTutor,
                            isLoading = false
                        )
                    }
                }
            } catch (e: IOException) {
                _state.update { state ->
                    state.copy(isLoading = false, error = true)
                }
            } catch (e: HttpRetryException) {
                _state.update { state ->
                    state.copy(isLoading = false, error = true)
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(isLoading = false, error = true)
                }
            }
        }
    }

    fun onAction(action: AboutAction) {
        viewModelScope.launch {
            when (action) {
                is AboutAction.ChangeAbout -> {
                    _state.update { state -> state.copy(about = action.about) }
                }

                is AboutAction.ChangeEducation -> {
                    _state.update { state -> state.copy(education = action.education.toDomain()) }
                }

                is AboutAction.ChangeSpecialization -> {
                    _state.update { state ->
                        state.copy(
                            education = state.education?.copy(
                                specialization = action.spec
                            )
                        )
                    }
                }

                is AboutAction.UpdateShowChangeAbout -> {
                    _state.update { state -> state.copy(aboutIsChanging = action.isChanging) }
                }

                is AboutAction.UpdateShowChangeOptions -> {
                    _state.update { state -> state.copy(changeOptionsIsExpanded = action.isExpanded) }
                }

                is AboutAction.UpdateShowEducationList -> {
                    _state.update { state -> state.copy(educationListIsExpanded = action.isExpanded) }
                }

                AboutAction.DismissAbout -> TODO()
                AboutAction.NavigateBack -> {
                    _navigationEvents.emit(AboutNavigationEvent.NavigateBack)
                }

                AboutAction.RefreshAction -> TODO()
                AboutAction.RetryAction -> TODO()
                AboutAction.SaveAbout -> TODO()
                AboutAction.DismissAboutConfirmed -> TODO()
                AboutAction.SaveAboutConfirmed -> TODO()

                AboutAction.UpdateShowChangePhoto -> TODO()
            }
        }
    }

}
package com.nailorsh.repeton.features.about.presentation.viewmodel

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel
import com.nailorsh.repeton.features.about.data.AboutRepository
import com.nailorsh.repeton.features.about.data.mappers.toDomain
import com.nailorsh.repeton.features.about.data.model.AboutContactItem
import com.nailorsh.repeton.features.about.data.model.AboutUpdatedData
import com.nailorsh.repeton.features.about.data.model.EducationItem
import com.nailorsh.repeton.features.about.data.model.LanguageItem
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
import java.util.Locale
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

    val languageSearchQuery: String = "",

    val education: Education? = null,
    val languagesWithLevels: List<LanguageWithLevel>? = null,
    val contacts: List<AboutContactItem> = emptyList(),

    val languagesList: List<LanguageItem> = emptyList(),
    val languageLevelList: List<LanguageLevel> = LanguageLevel.values().asList(),
    val educationList: List<EducationItem> = emptyList(),

    val changeOptionsIsExpanded: Boolean = false,
    val educationListIsExpanded: Boolean = false,
    val aboutIsChanging: Boolean? = null,
    val dataChanged: Boolean = false,
    val showSavingDialogue: Boolean = false,
)

sealed interface AboutAction {
    object NavigateBack : AboutAction
    data class SaveAbout(val about: String) : AboutAction
    data class ChangeSpecialization(val spec: String) : AboutAction
    data class ChangeEducation(val education: EducationItem) : AboutAction
    data class UpdateSearchQuery(val searchQuery: String) : AboutAction
    data class AddLanguage(val languageItem: LanguageItem) : AboutAction
    data class RemoveLanguage(val languageWithLevel: LanguageWithLevel) : AboutAction
    data class UpdateLanguageLevel(
        val languageWithLevel: LanguageWithLevel,
        val languageLvl: LanguageLevel
    ) :
        AboutAction

    data class UpdateContact(
        val contact: AboutContactItem,
        val value: String
    ) : AboutAction

    data class UpdateImage(val image: Uri) : AboutAction
    object SaveChanges : AboutAction
    object UpdateLanguagesList : AboutAction
    object DismissAbout : AboutAction
    object RetryAction : AboutAction

    data class UpdateShowChangeOptions(val isExpanded: Boolean) : AboutAction

    data class UpdateShowChangeAbout(val isChanging: Boolean) : AboutAction

    data class UpdateShowEducationList(val isExpanded: Boolean) : AboutAction

    data class UpdateShowSavingDialogue(val isSaving: Boolean) : AboutAction

}

sealed class AboutUiEvent(@StringRes val msg: Int) {
    object TooMuchLessons : AboutUiEvent(R.string.about_screen_too_much_lessons)
    object LanguageAlreadyAdded : AboutUiEvent(R.string.about_screen_language_already_added)
    object SaveError : AboutUiEvent(R.string.about_screen_couldnt_update_data)

    object TooMuchSymbolsInBio : AboutUiEvent(R.string.about_screen_too_much_symbols_in_bio)
}

sealed interface AboutNavigationEvent {
    object NavigateBack : AboutNavigationEvent

    object ChangedSuccessful : AboutNavigationEvent
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

    private lateinit var languagesList: List<LanguageItem>
    private lateinit var startData: AboutUpdatedData

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            _state.value = AboutState()
            try {
                withContext(Dispatchers.IO) {
                    val userData = aboutRepository.getUserData()
                    languagesList = aboutRepository.getLanguages()
                    val educationList = aboutRepository.getEducation()
                    startData = AboutUpdatedData(
                        photoSrc = userData.photoSrc,
                        about = userData.about ?: "",
                        education = userData.education,
                        languagesWithLevels = userData.languagesWithLevels,
                        contacts = userData.contacts
                    )
                    _state.update { state ->
                        AboutState(
                            photoSrc = userData.photoSrc,
                            name = userData.name,
                            surname = userData.surname,
                            about = userData.about ?: "",
                            aboutNew = userData.about ?: "",
                            education = userData.education,
                            languagesWithLevels = userData.languagesWithLevels,
                            contacts = userData.contacts,
                            languagesList = languagesList,
                            educationList = educationList,
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

    private suspend fun saveChanges() = withContext(Dispatchers.IO) {
        try {
            aboutRepository.updateAboutData(
                AboutUpdatedData(
                    about = if (_state.value.about != startData.about) _state.value.about else null,
                    photoSrc = if (_state.value.photoSrc != startData.photoSrc) _state.value.photoSrc else null,
                    education = if (_state.value.education != startData.education) _state.value.education else null,
                    languagesWithLevels = if (_state.value.languagesWithLevels != startData.languagesWithLevels) _state.value.languagesWithLevels else null,
                    contacts = if (_state.value.contacts != startData.contacts) _state.value.contacts else null
                )
            )
        } catch (e: Exception) {
            _uiEvents.emit(AboutUiEvent.SaveError)
            return@withContext
        }
        _navigationEvents.emit(AboutNavigationEvent.ChangedSuccessful)
    }

    fun onAction(action: AboutAction) {
        viewModelScope.launch {
            when (action) {
                AboutAction.SaveChanges -> {
                    onAction(AboutAction.UpdateShowSavingDialogue(true))
                    saveChanges()
                    onAction(AboutAction.UpdateShowSavingDialogue(false))
                }

                is AboutAction.UpdateShowSavingDialogue -> {
                    _state.update { state -> state.copy(showSavingDialogue = action.isSaving) }
                }

                is AboutAction.SaveAbout -> {
                    if (action.about.length > 200) {
                        _uiEvents.emit(AboutUiEvent.TooMuchSymbolsInBio)
                    } else
                        _state.update { state ->
                            state.copy(
                                about = action.about,
                                aboutIsChanging = false
                            )
                        }
                }

                is AboutAction.ChangeEducation -> {
                    _state.update { state -> state.copy(education = action.education.toDomain()) }
                }

                is AboutAction.UpdateImage -> {
                    _state.update { state -> state.copy(photoSrc = action.image.toString()) }
                }

                is AboutAction.UpdateContact -> {
                    _state.update { state ->
                        state.copy(
                            contacts = state.contacts.map { contact ->
                                when (contact) {
                                    is AboutContactItem.Other -> {
                                        if (action.contact is AboutContactItem.Other) {
                                            AboutContactItem.Other(action.value)
                                        } else {
                                            contact
                                        }
                                    }
                                    is AboutContactItem.Telegram -> {
                                        if (action.contact is AboutContactItem.Telegram) {
                                            AboutContactItem.Telegram(action.value)
                                        } else {
                                            contact
                                        }
                                    }
                                    is AboutContactItem.WhatsApp -> {
                                        if (action.contact is AboutContactItem.WhatsApp) {
                                            AboutContactItem.WhatsApp(action.value)
                                        } else {
                                            contact
                                        }
                                    }
                                }
                            }
                        )
                    }
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

                is AboutAction.AddLanguage -> {
                    println()
                    if (_state.value.languagesWithLevels != null && _state.value.languagesWithLevels!!.size == 5) {
                        _uiEvents.emit(AboutUiEvent.TooMuchLessons)
                    } else if (_state.value.languagesWithLevels != null && !_state.value.languagesWithLevels!!.none { languageWithLevel ->
                            languageWithLevel.language.id == action.languageItem.id
                        }) {
                        _uiEvents.emit(AboutUiEvent.LanguageAlreadyAdded)
                    } else
                        _state.update { state ->
                            state.copy(
                                languagesWithLevels = (state.languagesWithLevels
                                    ?: emptyList()).plus(
                                    LanguageWithLevel(
                                        Language(
                                            id = action.languageItem.id,
                                            name = action.languageItem.name
                                        ),
                                        level = LanguageLevel.OTHER
                                    )
                                )
                            )
                        }
                }

                is AboutAction.RemoveLanguage -> {
                    _state.update { state ->
                        state.copy(
                            languagesWithLevels = state.languagesWithLevels?.minusElement(action.languageWithLevel)
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

                is AboutAction.UpdateSearchQuery -> {
                    _state.update { state ->
                        state.copy(
                            languageSearchQuery = action.searchQuery
                        )
                    }
                    onAction(AboutAction.UpdateLanguagesList)
                }

                is AboutAction.UpdateLanguageLevel -> {
                    _state.update { state ->
                        state.copy(languagesWithLevels = state.languagesWithLevels?.map {
                            if (it == action.languageWithLevel) {
                                it.copy(level = action.languageLvl)
                            } else {
                                it
                            }
                        })
                    }
                }

                AboutAction.NavigateBack -> {
                    _navigationEvents.emit(AboutNavigationEvent.NavigateBack)
                }

                AboutAction.UpdateLanguagesList -> {
                    _state.update { state ->
                        state.copy(
                            languagesList = languagesList.filter {
                                it.name.lowercase(Locale.getDefault()).startsWith(
                                    prefix = state.languageSearchQuery.lowercase(
                                        Locale.getDefault()
                                    )
                                )
                            }
                        )
                    }
                }

                AboutAction.RetryAction -> getData()
                AboutAction.DismissAbout -> _state.update { state ->
                    state.copy(aboutIsChanging = false)
                }
            }
        }
    }

}
package com.nailorsh.repeton.features.about.presentation.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingDialogue
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutBioCard
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutBottomSheet
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutContacts
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutEducation
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutHeader
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutLanguages
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutTopBar
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutAction
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutState
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    state: AboutState,
    uiEvents: Flow<AboutUiEvent>,
    onAction: (AboutAction) -> Unit
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )

    var showChangeOptions by rememberSaveable {
        mutableStateOf(false)
    }


    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { image ->
        image?.let {
            onAction(AboutAction.UpdateImage(it))
        }
    }

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvents.collect { uiEvent ->
                snackbarHostState.showSnackbar(
                    message = context.getString(uiEvent.msg),
                    withDismissAction = true
                )
            }
        }
    }

    LaunchedEffect(key1 = state.aboutIsChanging) {
        state.aboutIsChanging?.let {
            if (it) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    if (state.showSavingDialogue) {
        Dialog(onDismissRequest = { }) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            AboutBottomSheet(
                text = state.about,
                onUpdateAbout = { onAction(AboutAction.SaveAbout(it)) },
                onDismiss = { onAction(AboutAction.DismissAbout) }
            )
        },
        sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = false,
        topBar = {
            AboutTopBar(
                isExpanded = showChangeOptions,
                showChangeOptions = { showChangeOptions = it },
                onChangeAbout = { onAction(AboutAction.UpdateShowChangeAbout(true)) },
                onChangePhoto = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onNavigateBack = { onAction(AboutAction.NavigateBack) },
                onSaveChanges = { onAction(AboutAction.SaveChanges) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error) {
            ErrorScreen(retryAction = { onAction(AboutAction.RetryAction) })
        } else {
            if (state.showSavingDialogue) {
                LoadingDialogue()
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .imePadding()
            ) {
                item {
                    AboutHeader(
                        name = state.name,
                        surname = state.surname,
                        photoSrc = state.photoSrc,
                        isTutor = state.isTutor,
                        onImageClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }
                item {
                    AboutBioCard(
                        biography = state.about,
                        onBioClicked = { onAction(AboutAction.UpdateShowChangeAbout(true)) })
                }
                item {
                    AboutContacts(
                        contacts = state.contacts,
                        onContactUpdate = { contact, value ->
                            onAction(
                                AboutAction.UpdateContact(
                                    contact,
                                    value
                                )
                            )
                        })
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 24.dp, bottom = 18.dp, start = 48.dp, end = 48.dp
                        )
                    )
                }
                item {
                    AboutLanguages(
                        searchQuery = state.languageSearchQuery,
                        languages = state.languagesList,
                        addedLanguageWithLevels = state.languagesWithLevels,
                        languageLevels = state.languageLevelList,
                        onQueryChange = { onAction(AboutAction.UpdateSearchQuery(it)) },
                        onSearch = { onAction(AboutAction.UpdateLanguagesList) },
                        onAddLanguage = { onAction(AboutAction.AddLanguage(it)) },
                        onRemoveLanguage = { onAction(AboutAction.RemoveLanguage(it)) },
                        updateLanguageLevel = { lang, lvl ->
                            onAction(
                                AboutAction.UpdateLanguageLevel(
                                    lang,
                                    lvl
                                )
                            )
                        },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 24.dp, bottom = 18.dp, start = 48.dp, end = 48.dp
                        )
                    )
                }

                item {
                    AboutEducation(
                        education = state.education?.type?.value,
                        specialization = state.education?.specialization,
                        educationsList = state.educationList,
                        onEducationChange = { onAction(AboutAction.ChangeEducation(it)) },
                        onSpecializationChange = { onAction(AboutAction.ChangeSpecialization(it)) },
                        isExpanded = state.educationListIsExpanded,
                        onExpandedChange = { onAction(AboutAction.UpdateShowEducationList(it)) },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewAboutScreen() {
    AboutScreen(
        state = AboutState(
            isLoading = false, error = false,
            education = Education(id = Id("-1"), type = EducationType.BACHELOR),
            languagesWithLevels = listOf(
                LanguageWithLevel(
                    Language(
                        id = Id("-1"),
                        name = "Английский"
                    ), level = LanguageLevel.B2
                )
            ),
            about = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ipsum has been the industry's standard dummy text ever since"
        ), onAction = {}, uiEvents = flowOf()
    )
}
package com.nailorsh.repeton.features.newlesson.presentation.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.LessonTopBar
import com.nailorsh.repeton.core.ui.components.images.ImageSliderDialogue
import com.nailorsh.repeton.core.util.cameraRequest
import com.nailorsh.repeton.core.util.fileAttachmentRequest
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.AdditionalMaterialsTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.DescriptionTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.HomeworkTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.NewLessonBottomSheet
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.NewLessonImageSlider
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondAction
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUIEvent
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


@Composable
fun NewLessonScreenSecond(
    uiState: NewLessonSecondUIState,
    uiEventChannel: SharedFlow<NewLessonSecondUIEvent>,
    onAction: (NewLessonSecondAction) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            NewLessonSecondUIState.Error -> {}
            NewLessonSecondUIState.Loading -> CircularProgressIndicator()
            is NewLessonSecondUIState.Success -> NewLessonScreenSecondContent(
                state = uiState.state,
                uiEventChannel = uiEventChannel,
                onAction = onAction,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewLessonScreenSecondContent(
    state: NewLessonSecondState,
    uiEventChannel: Flow<NewLessonSecondUIEvent>,
    onAction: (NewLessonSecondAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()

    val images = rememberUpdatedState(newValue = state.imageAttachments)
    val imageSliderPagerState = rememberPagerState {
        images.value.size
    }
    val dialogueSlidePagerState = rememberPagerState {
        images.value.size
    }


    val coroutineScope = rememberCoroutineScope()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { imageList ->
        imageList.forEach {
            onAction(
                NewLessonSecondAction.AddImageAttachment(it)
            )
        }
    }
    Log.d("NEW", dialogueSlidePagerState.currentPage.toString())


    val cameraRequest = cameraRequest(
        onImageCaptured = { onAction(NewLessonSecondAction.AddImageAttachment(it)) },
        onPermissionDenied = {
            onAction(NewLessonSecondAction.CameraRequestFail)
        }
    )

    val onClickAttachFile = fileAttachmentRequest(
        onFileChosen = { onAction(NewLessonSecondAction.AttachFileSuccess(it)) },
        onFileChoosingFailed = {
            onAction(NewLessonSecondAction.AttachFileFail)
        }
    )

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEventChannel.collect { uiEvent ->
                snackbarHostState.showSnackbar(
                    message = context.getString(uiEvent.msg),
                    withDismissAction = true
                )
            }
        }
    }

    LaunchedEffect(key1 = dialogueSlidePagerState.currentPage) {
        imageSliderPagerState.animateScrollToPage(dialogueSlidePagerState.currentPage)
    }


    Scaffold(
        topBar = {
            LessonTopBar(
                onNavigateBack = { onAction(NewLessonSecondAction.NavigateBack) },
                title = stringResource(id = R.string.new_lesson_screen_headline)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(NewLessonSecondAction.SaveLesson) }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (state.showImageTypeDialogue) {
            NewLessonBottomSheet(
                cameraRequest = {
                    if (state.imageAttachments.size < 10) {
                        cameraRequest()
                    } else {
                        onAction(NewLessonSecondAction.TooMuchImages)
                    }

                    onAction(
                        NewLessonSecondAction.UpdateShowImageTypeDialogue(
                            false
                        )
                    )
                },
                photoPickerRequest = {
                    if (state.imageAttachments.size < 10) {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        onAction(NewLessonSecondAction.TooMuchImages)
                    }

                    onAction(
                        NewLessonSecondAction.UpdateShowImageTypeDialogue(
                            false
                        )
                    )
                },
                sheetState = sheetState,
                onDismissRequest = {
                    onAction(
                        NewLessonSecondAction.UpdateShowImageTypeDialogue(
                            false
                        )
                    )
                }
            )
        }
        if (state.showImageDialogue) {
            ImageSliderDialogue(
                onDismissRequest = {
                    onAction(
                        NewLessonSecondAction.UpdateShowImageDialogue(
                            false
                        )
                    )
                },
                images = images.value,
                pagerState = dialogueSlidePagerState
            )
        }
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceBright)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        focusManager.clearFocus()
                    }
                )
                .padding(paddingValues)
                .padding(start = 32.dp, end = 32.dp, top = 16.dp)
        ) {

            // Поле добавления описания
            item {
                DescriptionTextField(
                    description = state.description,
                    onDescriptionChange = { onAction(NewLessonSecondAction.UpdateDescription(it)) }
                )
            }
            // Поле добавления домашнего задания
            item {
                HomeworkTextField(
                    homeworkText = state.homeworkText,
                    onHomeworkTextChange = {
                        onAction(
                            NewLessonSecondAction.UpdateHomeworkText(
                                it
                            )
                        )
                    },
                    onClickCameraRequest = {
                        onAction(
                            NewLessonSecondAction.UpdateShowImageTypeDialogue(
                                true
                            )
                        )
                    },
                    onClickAttachFile = {
                        onClickAttachFile()
                    }
                )
            }
            if (images.value.isNotEmpty()) {
                item {
                    NewLessonImageSlider(
                        imageList = images.value,
                        pagerState = imageSliderPagerState,
                        coroutineScope = coroutineScope,
                        onImageClick = {
                            onAction(NewLessonSecondAction.UpdateShowImageDialogue(true))
                            coroutineScope.launch {
                                dialogueSlidePagerState.scrollToPage(imageSliderPagerState.currentPage)
                            }
                        },
                        onDeleteImage = {

                            coroutineScope.launch {
                                val currentPage = imageSliderPagerState.currentPage
                                val newSize = images.value.size - 1
                                if (currentPage >= newSize) {
                                    imageSliderPagerState.scrollToPage(maxOf(0, newSize - 1))
                                    dialogueSlidePagerState.scrollToPage(
                                        maxOf(
                                            0,
                                            newSize - 1
                                        )
                                    )
                                }
                            }.let { _ -> onAction(NewLessonSecondAction.RemoveImageAttachment(it)) }


                        },
                        updateImageDescription = { text, index ->
                            onAction(NewLessonSecondAction.UpdateImageText(text, index))
                        }
                    )
                }
            }

            // Поле добавления дополнительных материалов
            item {
                AdditionalMaterialsTextField(
                    additionalMaterials = state.additionalMaterials,
                    onAdditionalMaterialsChange = {
                        onAction(
                            NewLessonSecondAction.UpdateAdditionalMaterials(
                                it
                            )
                        )
                    }
                )
            }


        }
    }

}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NewLessonScreenSecondPreview() {

}
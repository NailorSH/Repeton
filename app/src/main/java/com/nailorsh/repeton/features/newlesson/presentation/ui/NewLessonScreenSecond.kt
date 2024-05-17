package com.nailorsh.repeton.features.newlesson.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.core.util.cameraRequest
import com.nailorsh.repeton.core.util.fileAttachmentRequest
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.NewLessonTopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.AdditionalMaterialsTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.DescriptionTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.HomeworkTextField
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondAction
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUIEvent
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow


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

@Composable
fun NewLessonScreenSecondContent(
    state: NewLessonSecondState,
    uiEventChannel: Flow<NewLessonSecondUIEvent>,
    onAction: (NewLessonSecondAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
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

    val cameraRequest = cameraRequest(
        onImageCaptured = { onAction(NewLessonSecondAction.CameraRequestSuccess(it)) },
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
    Scaffold(
        topBar = {
            NewLessonTopBar(onNavigateBack = { onAction(NewLessonSecondAction.NavigateBack) })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(NewLessonSecondAction.SaveLesson) }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
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
                .verticalScroll(scrollState)
        ) {

            // Поле добавления описания
            DescriptionTextField(
                description = state.description,
                onDescriptionChange = { onAction(NewLessonSecondAction.UpdateDescription(it)) }
            )
            // Поле добавления домашнего задания
            HomeworkTextField(
                homeworkText = state.homeworkText,
                onHomeworkTextChange = {
                    onAction(
                        NewLessonSecondAction.UpdateHomeworkText(
                            it
                        )
                    )
                },
                onClickCameraRequest = { cameraRequest() },
                onClickAttachFile = { onClickAttachFile() }
            )

            // Поле добавления дополнительных материалов
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


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NewLessonScreenSecondPreview() {

}
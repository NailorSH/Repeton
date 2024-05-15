package com.nailorsh.repeton.features.newlesson.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.util.cameraRequest
import com.nailorsh.repeton.core.util.fileAttachmentRequest
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.NewLessonTopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.AdditionalMaterialsTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.DescriptionTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.HomeworkTextField
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondCallBack
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUIEvent
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUIState
import kotlinx.coroutines.flow.Flow


@Composable
fun NewLessonScreenSecond(
    uiState: NewLessonSecondUIState,
    uiEventChannel: Flow<NewLessonSecondUIEvent>,
    onCallback: (NewLessonSecondCallBack) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            NewLessonSecondUIState.Error -> {}
            NewLessonSecondUIState.Loading -> CircularProgressIndicator()
            is NewLessonSecondUIState.Success -> NewLessonScreenSecondContent(
                state = uiState.state,
                uiEventChannel = uiEventChannel,
                onCallback = onCallback,
            )
        }
    }
}

@Composable
fun NewLessonScreenSecondContent(
    state: NewLessonSecondState,
    uiEventChannel: Flow<NewLessonSecondUIEvent>,
    onCallback: (NewLessonSecondCallBack) -> Unit
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
        onImageCaptured = { onCallback(NewLessonSecondCallBack.CameraRequestSuccess(it)) },
        onPermissionDenied = {
            onCallback(NewLessonSecondCallBack.CameraRequestFail)
        }
    )

    val onClickAttachFile = fileAttachmentRequest(
        onFileChosen = { onCallback(NewLessonSecondCallBack.AttachFileSuccess(it)) },
        onFileChoosingFailed = {
            onCallback(NewLessonSecondCallBack.AttachFileFail)
        }
    )
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
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
        ) {

            NewLessonTopBar(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {

                // Поле добавления описания
                DescriptionTextField(
                    description = state.description,
                    onDescriptionChange = { onCallback(NewLessonSecondCallBack.UpdateDescription(it)) }
                )
                // Поле добавления домашнего задания
                HomeworkTextField(
                    homeworkText = state.homeworkText,
                    onHomeworkTextChange = {
                        onCallback(
                            NewLessonSecondCallBack.UpdateHomeworkText(
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
                        onCallback(
                            NewLessonSecondCallBack.UpdateAdditionalMaterials(
                                it
                            )
                        )
                    }
                )


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onCallback(NewLessonSecondCallBack.NavigateBack) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Button(

                        onClick = {
                            onCallback(NewLessonSecondCallBack.SaveLesson)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.new_lesson_screen_save_new_lesson),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
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
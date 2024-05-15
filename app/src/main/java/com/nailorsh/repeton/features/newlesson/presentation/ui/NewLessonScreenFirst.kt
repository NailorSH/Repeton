package com.nailorsh.repeton.features.newlesson.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.util.CalendarDialog
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.NewLessonTopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.DateTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.EndTimeTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.LessonTimePicker
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.StartTimeTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.SubjectTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.TopicTextField
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonCallback
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUIEvent
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

const val TAG = "NEW_LESSON"


@Composable
fun NewLessonScreen(
    uiState: NewLessonFirstUiState,
    onCallback: (NewLessonCallback) -> Unit,
    eventChannel: Flow<NewLessonUIEvent>,
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            NewLessonFirstUiState.Error -> {}
            NewLessonFirstUiState.Loading -> CircularProgressIndicator()
            is NewLessonFirstUiState.Success -> NewLessonScreenContent(
                lessonState = uiState.state,
                onCallback = onCallback,
                uiEventChannel = eventChannel
            )
        }
    }
}

@Composable
fun NewLessonScreenContent(
    lessonState: NewLessonFirstState,
    onCallback: (NewLessonCallback) -> Unit,
    uiEventChannel: Flow<NewLessonUIEvent>,
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEventChannel.collect { uiEvent ->
                when (uiEvent) {
                    NewLessonUIEvent.DateError -> snackbarHostState.showSnackbar(message = "Date Error")
                    NewLessonUIEvent.EndTimeError -> snackbarHostState.showSnackbar(message = "End Time Error")
                    NewLessonUIEvent.StartTimeError -> snackbarHostState.showSnackbar(message = "Start Time Error")
                    NewLessonUIEvent.SubjectError -> snackbarHostState.showSnackbar(message = "Subject Error")
                    NewLessonUIEvent.TopicError -> snackbarHostState.showSnackbar(message = "Topic Error")
                }
            }
        }
    }


    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (lessonState.showDatePicker) {
            CalendarDialog(
                date = lessonState.startTime,
                onDateChange = {
                    if (it != null) {
                        onCallback(
                            NewLessonCallback.UpdateDate(
                                Instant
                                    .ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            )
                        )
                    }
                },
                onDismissRequest = { onCallback(NewLessonCallback.UpdateShowDatePicker(false)) }
            )

        }

        if (lessonState.showTimePickerStart) {
            LessonTimePicker(
                onCancel = { onCallback(NewLessonCallback.UpdateShowTimePickerStart(false)) },
                onConfirm = {
                    onCallback(
                        NewLessonCallback.UpdateStartTime(
                            lessonState.startTime.withHour(it.get(Calendar.HOUR_OF_DAY))
                                .withMinute(it.get(Calendar.MINUTE))
                        )
                    )

                },
                initialTime = lessonState.startTime.toLocalTime()
            )

        }

        if (lessonState.showTimePickerEnd) {

            LessonTimePicker(
                onCancel = { onCallback(NewLessonCallback.UpdateShowTimePickerEnd(false)) },
                onConfirm = {
                    onCallback(
                        NewLessonCallback.UpdateEndTime(
                            lessonState.endTime.withHour(it.get(Calendar.HOUR_OF_DAY))
                                .withMinute(it.get(Calendar.MINUTE))
                        )
                    )
                },
                initialTime = lessonState.endTime.toLocalTime(),
                startTime = lessonState.startTime.toLocalTime()
            )
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceBright)
                .padding(paddingValues)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        onCallback(NewLessonCallback.UpdateShowDropDownMenu(false))
                        focusManager.clearFocus()
                    }
                )
                .verticalScroll(scrollState)
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
            ) {

                SubjectTextField(
                    subject = lessonState.subjectText,
                    onSubjectChange = {
                        onCallback(NewLessonCallback.UpdateSubjectText(it))
                    },
                    subjects = lessonState.loadedSubjects,
                    expanded = lessonState.showDropdownMenu,
                    onExpandedChange = { onCallback(NewLessonCallback.UpdateShowDropDownMenu(it)) },
                    onChangeError = { },
                    isError = false
                )

                TopicTextField(
                    topic = lessonState.topic,
                    onTopicChange = { onCallback(NewLessonCallback.UpdateTopicText(it)) }
                )

                DateTextField(
                    date = lessonState.startTime.toLocalDate(),
                    firstSet = !lessonState.showTimePickerStartTextField,
                    onClick = {
                        onCallback(NewLessonCallback.UpdateShowDatePicker(true))
                    },
                    isError = false
                )
                AnimatedVisibility(
                    visible = lessonState.showTimePickerStartTextField,
                    enter = slideInHorizontally() + fadeIn(initialAlpha = 0.3f)
                ) {
                    StartTimeTextField(
                        time = lessonState.startTime.toLocalTime(),
                        firstSet = !lessonState.showTimePickerStartTextField,
                        onClick = {
                            onCallback(NewLessonCallback.UpdateShowTimePickerStart(true))
                        },
                        isError = false
                    )
                }

                AnimatedVisibility(
                    visible = lessonState.showTimePickerEndTextField,
                    enter = slideInHorizontally() + fadeIn(initialAlpha = 0.3f)
                ) {
                    EndTimeTextField(
                        time = lessonState.endTime.toLocalTime(),
                        onClick = { onCallback(NewLessonCallback.UpdateShowTimePickerEnd(true)) },
                        isError = false
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onCallback(NewLessonCallback.NavigateBack)
                        },
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
                        enabled = lessonState.showTimePickerEndTextField,
                        onClick = {
                            focusManager.clearFocus()
                            onCallback(NewLessonCallback.SaveData)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }

    }


}


@Preview
@Composable
fun NewLessonScreenPreview() {

//    NewLessonScreen(
//        lessonState = NewLessonViewModel(FakeNewLessonRepository()).state.collectAsState().value,
//        onNavigateBack = {},
//        onSaveRequiredFields = { _, _, _, _ -> },
//        onNavigateNext = {},
//        filteredSubjects = listOf(),
//        onFilterSubjects = {}
//    )

}
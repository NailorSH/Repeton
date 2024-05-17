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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.util.CalendarDialog
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.NewLessonTopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.AddUserDialogue
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.AddedStudents
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.DateTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.EndTimeTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.LessonTimePicker
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.StartTimeTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.SubjectTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.TopicTextField
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstAction
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonFirstUIState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

const val TAG = "NEW_LESSON"


@Composable
fun NewLessonScreen(
    uiState: NewLessonFirstUIState,
    onAction: (NewLessonFirstAction) -> Unit,
    uiEventChannel: SharedFlow<NewLessonUIEvent>,
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            NewLessonFirstUIState.Error -> {}
            NewLessonFirstUIState.Loading -> CircularProgressIndicator()
            is NewLessonFirstUIState.Success -> NewLessonScreenContent(
                lessonState = uiState.state,
                onAction = onAction,
                uiEventChannel = uiEventChannel
            )
        }
    }
}

@Composable
fun NewLessonScreenContent(
    lessonState: NewLessonFirstState,
    onAction: (NewLessonFirstAction) -> Unit,
    uiEventChannel: Flow<NewLessonUIEvent>,
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEventChannel.collect { uiEvent ->
                snackbarHostState.showSnackbar(
                    message = context.getString(uiEvent.errorMsg),
                    withDismissAction = true
                )
            }
        }
    }


    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            NewLessonTopBar(
                onNavigateBack = {
                    onAction(NewLessonFirstAction.NavigateBack)
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = lessonState.showTimePickerStartTextField && lessonState.showTimePickerEndTextField) {
                FloatingActionButton(onClick = { onAction(NewLessonFirstAction.SaveData) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_alt),
                        contentDescription = null
                    )
                }
            }

        }
    ) { paddingValues ->
        if (lessonState.showDatePicker) {
            CalendarDialog(
                date = lessonState.startTime,
                onDateChange = {
                    if (it != null) {
                        onAction(
                            NewLessonFirstAction.UpdateDate(
                                Instant
                                    .ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            )
                        )
                    }
                },
                onDismissRequest = { onAction(NewLessonFirstAction.UpdateShowDatePicker(false)) }
            )

        }

        if (lessonState.showAddUserDialogue) {
            AddUserDialogue(
                students = lessonState.allStudents,
                onAddUser = {
                    onAction(NewLessonFirstAction.AddUser(it))
                    onAction(NewLessonFirstAction.UpdateShowAddUserDialogue(false))
                },
                onDismissRequest = { onAction(NewLessonFirstAction.UpdateShowAddUserDialogue(false)) }
            )
        }

        if (lessonState.showTimePickerStart) {
            LessonTimePicker(
                onCancel = { onAction(NewLessonFirstAction.UpdateShowTimePickerStart(false)) },
                onConfirm = {
                    onAction(
                        NewLessonFirstAction.UpdateStartTime(
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
                onCancel = { onAction(NewLessonFirstAction.UpdateShowTimePickerEnd(false)) },
                onConfirm = {
                    onAction(
                        NewLessonFirstAction.UpdateEndTime(
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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceBright)
                .padding(paddingValues)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        onAction(NewLessonFirstAction.UpdateShowDropDownMenu(false))
                        focusManager.clearFocus()
                    }
                )
                .verticalScroll(scrollState)
        ) {
            Text("Ученики", style = MaterialTheme.typography.headlineMedium)
            AddedStudents(
                students = lessonState.pickedStudents,
                onRemoveUser = { onAction(NewLessonFirstAction.RemoveUser(it)) },
                onAddUserShowDialogue = { onAction(NewLessonFirstAction.UpdateShowAddUserDialogue(true)) }
            )
            HorizontalDivider()
            Text("Информация", style = MaterialTheme.typography.headlineMedium)
            SubjectTextField(
                subject = lessonState.subjectText,
                onSubjectChange = {
                    onAction(NewLessonFirstAction.UpdateSubjectText(it))
                },
                subjects = lessonState.loadedSubjects,
                expanded = lessonState.showDropdownMenu,
                onExpandedChange = { onAction(NewLessonFirstAction.UpdateShowDropDownMenu(it)) },
                onChangeError = { },
                isError = false
            )

            TopicTextField(
                topic = lessonState.topic,
                onTopicChange = { onAction(NewLessonFirstAction.UpdateTopicText(it)) }
            )

            DateTextField(
                date = lessonState.startTime.toLocalDate(),
                firstSet = !lessonState.showTimePickerStartTextField,
                onClick = {
                    onAction(NewLessonFirstAction.UpdateShowDatePicker(true))
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
                        onAction(NewLessonFirstAction.UpdateShowTimePickerStart(true))
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
                    onClick = { onAction(NewLessonFirstAction.UpdateShowTimePickerEnd(true)) },
                    isError = false
                )
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
package com.nailorsh.repeton.features.newlesson.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Subject
import com.nailorsh.repeton.features.newlesson.data.FakeNewLessonRepository
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.NewLessonTopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.*
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import java.time.*
import java.util.*

const val TAG = "NEW_LESSON"

@Composable
fun NewLessonScreen(
    lessonState: NewLessonState,
    filteredSubjects: List<Subject>,
    onFilterSubjects: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onSaveRequiredFields: (String, String, LocalDateTime, LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showStartTimePickerTextField by remember { mutableStateOf(false) }
    var showEndTimePickerTextField by remember { mutableStateOf(false) }

    var localSubject by remember { mutableStateOf(lessonState.subject.subjectName) }
    var localTopic by remember { mutableStateOf(lessonState.topic) }
    var localStartTime by remember { mutableStateOf(lessonState.startTime) }
    var localEndTime by remember { mutableStateOf(lessonState.endTime) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(localSubject) {
        onFilterSubjects(localSubject)
    }

    var subjectError by remember { mutableStateOf(false) }


    LaunchedEffect(lessonState.uiState) {
        when (lessonState.uiState) {
            is NewLessonUiState.ErrorSaving -> {
                subjectError = lessonState.uiState.error
            }

            NewLessonUiState.SuccessSaving -> onNavigateNext()
            else -> {}
        }
    }

    val focusManager = LocalFocusManager.current


    val now = remember { LocalDateTime.now() }

    val (dateError, startTimeError, endTimeError) = remember {
        derivedStateOf {
            val dateErrorValue = now.toLocalDate() > localStartTime.toLocalDate()
            val startTimeErrorValue = if (now.toLocalDate() == localStartTime.toLocalDate()) {
                now.toLocalTime() > localStartTime.toLocalTime()
            } else {
                false
            }
            val endTimeErrorValue = if (!startTimeErrorValue) {
                localStartTime.toLocalTime() >= localEndTime.toLocalTime()
            } else {
                false
            }
            Triple(dateErrorValue, startTimeErrorValue, endTimeErrorValue)
        }
    }.value




    if (showDatePicker) {


        CalendarDialog(
            date = localStartTime,
            onDateChange = {
                if (it != null) {
                    localStartTime = localStartTime.with(
                        Instant
                            .ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    )
                }
                showDatePicker = false
                showStartTimePickerTextField = true
            },
            onDismissRequest = { showDatePicker = false }
        )

    }

    if (showStartTimePicker) {

        LessonTimePicker(
            onCancel = { showStartTimePicker = false },
            onConfirm = {
                localStartTime =
                    localStartTime.withHour(it.get(Calendar.HOUR_OF_DAY)).withMinute(it.get(Calendar.MINUTE))
                showStartTimePicker = false
                showEndTimePickerTextField = true
                localEndTime = localStartTime.plusMinutes(30)
                showEndTimePicker = true

            },
            initialTime = localStartTime.toLocalTime()
        )

    }

    if (showEndTimePicker) {

        LessonTimePicker(
            onCancel = { showEndTimePicker = false },
            onConfirm = {
                localEndTime = localEndTime.withHour(it.get(Calendar.HOUR_OF_DAY)).withMinute(it.get(Calendar.MINUTE))
                showEndTimePicker = false
            },
            initialTime = localEndTime.toLocalTime(),
            startTime = localStartTime.toLocalTime()
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceBright)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    expanded = false
                    focusManager.clearFocus()
                }
            )
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
                subject = localSubject,
                onSubjectChange = {
                    localSubject = it
                },
                subjects = filteredSubjects,
                expanded = expanded,
                onExpandedChange = { expanded = it },
                onChangeError = { subjectError = false },
                isError = subjectError
            )

            TopicTextField(
                topic = localTopic,
                onTopicChange = { localTopic = it }
            )

            DateTextField(
                date = localStartTime.toLocalDate(),
                firstSet = !showStartTimePickerTextField,
                onClick = {
                    showDatePicker = true
                },
                isError = dateError
            )
            AnimatedVisibility(
                visible = showStartTimePickerTextField,
                enter = slideInHorizontally() + fadeIn(initialAlpha = 0.3f)
            ) {
                StartTimeTextField(
                    time = localStartTime.toLocalTime(),
                    firstSet = !showEndTimePickerTextField,
                    onClick = {
                        showStartTimePicker = true
                    },
                    isError = startTimeError
                )
            }

            AnimatedVisibility(
                visible = showEndTimePickerTextField,
                enter = slideInHorizontally() + fadeIn(initialAlpha = 0.3f)
            ) {
                EndTimeTextField(
                    time = localEndTime.toLocalTime(),
                    onClick = { showEndTimePicker = true },
                    isError = endTimeError
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNavigateBack,
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
                    enabled = localSubject != "" && localTopic != ""
                            && showEndTimePickerTextField &&
                            !dateError && !startTimeError && !endTimeError,
                    onClick = {
                        // Fields validation
                        onSaveRequiredFields(localSubject, localTopic, localStartTime, localEndTime)
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


@Preview
@Composable
fun NewLessonScreenPreview() {

    NewLessonScreen(
        lessonState = NewLessonViewModel(FakeNewLessonRepository()).state.collectAsState().value,
        onNavigateBack = {},
        onSaveRequiredFields = { a, b, c, d -> },
        onNavigateNext = {},
        filteredSubjects = listOf(),
        onFilterSubjects = {}
    )

}
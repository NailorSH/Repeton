package com.nailorsh.repeton.features.newlesson.presentation.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.TopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.first.*
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import java.time.*
import java.util.*

const val TAG = "NEW_LESSON"

@Composable
fun NewLessonScreen(
    newLessonUiState: NewLessonUiState,
    subject : Subject,
    topic : String,
    startTime : LocalDateTime,
    endTime : LocalDateTime,
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

    var localSubject by remember { mutableStateOf(subject.subjectName) }

    var localTopic by remember { mutableStateOf(topic) }
    var localStartTime by remember { mutableStateOf(startTime) }
    var localEndTime by remember { mutableStateOf(endTime) }
    var expanded by remember { mutableStateOf(false) }
    var subjects by remember { mutableStateOf(listOf<Subject>()) }

    var subjectError by remember { mutableStateOf(false) }

    val filteredSubjects by remember {
        derivedStateOf {
            subjects.filter {
                it.subjectName.lowercase().startsWith(localSubject.lowercase())
            }.sortedWith(compareBy { it.subjectName })
        }

    }

    val focusManager = LocalFocusManager.current



    val (dateError, startTimeError, endTimeError) = remember {
        derivedStateOf {
            val dateErrorValue = LocalDate.now() > localStartTime.toLocalDate()
            val startTimeErrorValue = if (LocalDate.now() == localStartTime.toLocalDate()) {
                LocalTime.now() > localStartTime.toLocalTime()
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


    var errorToast by remember { mutableStateOf(false) }

    if (errorToast) {
        errorToast = false
        /* TODO Уведомление об ошибке */
    }

    LaunchedEffect(newLessonUiState) {
        when (newLessonUiState) {
            NewLessonUiState.Error -> {}
            is NewLessonUiState.ErrorSaving -> {
                subjectError = newLessonUiState.error
                subjects = newLessonUiState.subjects
                errorToast = true
            }

            NewLessonUiState.Loading -> {

            }

            is NewLessonUiState.Success -> {
                subjects = newLessonUiState.subjects
            }

            NewLessonUiState.SuccessSaving -> {
                /* TODO Сделать нормальное уведомление */
                onNavigateNext()
            }
        }
    }

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
                localStartTime = localStartTime.withHour(it.get(Calendar.HOUR_OF_DAY)).withMinute(it.get(Calendar.MINUTE))
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

        TopBar(
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
                    subjectError = false
                },
                subjects = filteredSubjects,
                expanded = expanded,
                onExpandedChange = { expanded = it },
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

@Composable
fun getColorsForTextField(error: Boolean): TextFieldColors {
    if (!error) {
        return OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        return OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.error,
            disabledBorderColor = MaterialTheme.colorScheme.error,
            disabledPlaceholderColor = MaterialTheme.colorScheme.error,
            disabledLabelColor = MaterialTheme.colorScheme.error,
            disabledLeadingIconColor = MaterialTheme.colorScheme.error,
            disabledTrailingIconColor = MaterialTheme.colorScheme.error,
            disabledSupportingTextColor = MaterialTheme.colorScheme.error
        )
    }
}


@Preview
@Composable
fun NewLessonScreenPreview() {

    NewLessonScreen(
        newLessonUiState = NewLessonViewModel(FakeNewLessonRepository()).newLessonUiState,
        onNavigateBack = {},
        onSaveRequiredFields = { a, b, c, d -> },
        onNavigateNext = {},
        startTime = LocalDateTime.now().plusMinutes(1),
        endTime = LocalDateTime.now().plusMinutes(30),
        subject = Subject(-1, ""),
        topic = ""
    )

}
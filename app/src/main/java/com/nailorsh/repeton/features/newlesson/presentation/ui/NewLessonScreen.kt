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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Subject
import com.nailorsh.repeton.core.ui.components.RepetonDivider
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.*
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import java.time.*
import java.util.*

const val TAG = "NEW_LESSON"

@Composable
fun NewLessonScreen(
    newLessonUiState: NewLessonUiState,
    onNavigateBack: () -> Unit,
    onSaveRequiredFields: (String, String, LocalDateTime, LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showStartTimePickerTextField by remember { mutableStateOf(false) }
    var showEndTimePickerTextField by remember { mutableStateOf(false) }

    var subject by remember { mutableStateOf("") }
    /* TODO сделать var subject_id : Int */


    var topic by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(LocalDateTime.now().plusMinutes(1)) }
    var endTime by remember { mutableStateOf(startTime.plusMinutes(30)) }
    var expanded by remember { mutableStateOf(false) }
    var subjects by remember { mutableStateOf(listOf<Subject>()) }

    var subjectError by remember { mutableStateOf(false) }

    val (dateError, startTimeError, endTimeError) = remember {
        derivedStateOf {
            val dateErrorValue = LocalDate.now() > startTime.toLocalDate()
            val startTimeErrorValue = if (LocalDate.now() == startTime.toLocalDate()) {
                LocalTime.now() > startTime.toLocalTime()
            } else {
                false
            }
            val endTimeErrorValue = if (!startTimeErrorValue) {
                startTime.toLocalTime() >= endTime.toLocalTime()
            } else {
                false
            }
            Triple(dateErrorValue, startTimeErrorValue, endTimeErrorValue)
        }
    }.value

    val focusManager = LocalFocusManager.current

    LaunchedEffect(newLessonUiState) {
        Log.d(TAG, newLessonUiState.toString())
        when (newLessonUiState) {
            NewLessonUiState.Error -> {}
            is NewLessonUiState.ErrorSaving -> {
                subjectError = newLessonUiState.error
            }

            NewLessonUiState.Loading -> {

            }

            is NewLessonUiState.Success -> {
                subjects = newLessonUiState.subjects
            }

            NewLessonUiState.SuccessSaving -> {
                /* TODO Сделать нормальное уведомление */
                onNavigateBack()
            }
        }
    }
    Log.d(TAG, "Loaded Subjects: $subjects")
    if (showDatePicker) {


        CalendarDialog(
            date = startTime,
            onDateChange = {
                if (it != null) {
                    startTime = startTime.with(
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
                startTime = startTime.withHour(it.get(Calendar.HOUR_OF_DAY)).withMinute(it.get(Calendar.MINUTE))
                showStartTimePicker = false
                showEndTimePickerTextField = true
                endTime = startTime.plusMinutes(30)
                showEndTimePicker = true

            },
            initialTime = startTime.toLocalTime()
        )

    }

    if (showEndTimePicker) {

        LessonTimePicker(
            onCancel = { showEndTimePicker = false },
            onConfirm = {
                endTime = endTime.withHour(it.get(Calendar.HOUR_OF_DAY)).withMinute(it.get(Calendar.MINUTE))
                showEndTimePicker = false
            },
            initialTime = endTime.toLocalTime(),
            startTime = startTime.toLocalTime()
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
        Spacer(modifier = Modifier.height(48.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            modifier = Modifier
                .width(dimensionResource(R.dimen.schedule_screen_button_width))
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = stringResource(R.string.new_lesson_screen_headline),
                    letterSpacing = 0.5.sp,
                )
            }

        }
        RepetonDivider(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = dimensionResource(R.dimen.padding_medium))
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {

            SubjectTextField(
                subject = subject,
                onSubjectChange = {
                    subject = it
                    subjectError = false
                },
                subjects = subjects,
                expanded = expanded,
                onExpandedChange = { expanded = it },
                isError = subjectError
            )

            TopicTextField(
                topic = topic,
                onTopicChange = { topic = it }
            )

            DateTextField(
                date = startTime.toLocalDate(),
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
                    time = startTime.toLocalTime(),
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
                    time = endTime.toLocalTime(),
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
                    enabled = subject != "" && topic != ""
                            && showEndTimePickerTextField &&
                            !dateError && !startTimeError && !endTimeError,
                    onClick = {
                        // Fields validation
                        onSaveRequiredFields(subject, topic, startTime, endTime)
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


//@Preview
//@Composable
//fun NewLessonScreenPreview() {
//    RepetonTheme {
//        NewLessonScreen(
//            newLessonUiState = NewLessonViewModel(FakeNewLessonRepository()).newLessonUiState,
//            onNavigateBack = {},
//            onSaveRequiredFields = {},
//        )
//    }
//}
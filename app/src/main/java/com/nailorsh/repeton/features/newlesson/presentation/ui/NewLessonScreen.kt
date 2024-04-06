package com.nailorsh.repeton.features.newlesson.presentation.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.common.data.models.Subject
import com.nailorsh.repeton.core.ui.components.RepetonDivider
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.newlesson.data.FakeNewLessonRepository
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.*
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

const val TAG = "NEW_LESSON"

@Composable
fun NewLessonScreen(
    newLessonUiState: NewLessonUiState,
    onNavigateBack: () -> Unit,
    onSaveRequiredFields: (String, String, LocalDateTime, LocalDateTime) -> Boolean,
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
    var startTime by remember { mutableStateOf(LocalDateTime.now()) }
    var endTime by remember { mutableStateOf(startTime.plusMinutes(30)) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var subjects = listOf<Subject>()
    var errors = List(5){ 0 }

    val focusManager = LocalFocusManager.current

    when (newLessonUiState) {
        NewLessonUiState.Error -> {}
        is NewLessonUiState.ErrorSaving -> errors = newLessonUiState.errors
        NewLessonUiState.Loading -> {}
        is NewLessonUiState.Success -> subjects = newLessonUiState.subjects
    }

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
                interactionSource = interactionSource,
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
                onSubjectChange = { subject = it },
                subjects = subjects,
                expanded = expanded,
                onExpandedChange = { expanded = it },
                isError = errors[NewLessonViewModel.ERROR_FOR_SUBJECT_TEXT_FIELD] == 1
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
                isError = errors[NewLessonViewModel.ERROR_FOR_DATE_TEXT_FIELD] == 1
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
                    isError = errors[NewLessonViewModel.ERROR_FOR_START_TIME_TEXT_FIELD] == 1
                )
            }

            AnimatedVisibility(
                visible = showEndTimePickerTextField,
                enter = slideInHorizontally() + fadeIn(initialAlpha = 0.3f)
            ) {
                EndTimeTextField(
                    time = endTime.toLocalTime(),
                    onClick = { showEndTimePicker = true },
                    isError = errors[NewLessonViewModel.ERROR_FOR_END_TIME_TEXT_FIELD] == 1
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
                    enabled = subject != "" && topic != "" && showEndTimePickerTextField,
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
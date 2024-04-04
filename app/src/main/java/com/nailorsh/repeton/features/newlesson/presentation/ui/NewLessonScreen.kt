package com.nailorsh.repeton.features.newlesson.presentation.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Lesson
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
    onSaveLessons: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }


    var subject by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(LocalDateTime.now()) }
    var endTime by remember { mutableStateOf(startTime.plusMinutes(90)) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }


    val focusManager = LocalFocusManager.current
    Log.d(TAG, showDatePicker.toString())

    val context = LocalContext.current

    if (showDatePicker) {


        CalendarDialog(
            date = startTime.toLocalDate(),
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
            },
            onDismissRequest = { showDatePicker = false }
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
                newLessonUiState = newLessonUiState,
                expanded = expanded,
                onExpandedChange = { expanded = it }
            )

            TopicTextField(
                topic = topic,
                onTopicChange = { topic = it }
            )

            DateTextField(
                date = startTime.toLocalDate(),
                onClick = { showDatePicker = true }
            )

            StartTimeTextField(
                time = startTime.toLocalTime(),
                onClick = { showStartTimePicker = true }
            )

            EndTimeTextField(
                time = endTime.toLocalTime(),
                onClick = { showEndTimePicker = true }
            )
        }

    }
}


@Preview
@Composable
fun NewLessonScreenPreview() {
    RepetonTheme {
        NewLessonScreen(
            newLessonUiState = NewLessonViewModel(FakeNewLessonRepository()).newLessonUiState,
            onNavigateBack = {},
            onSaveLessons = {},
        )
    }
}
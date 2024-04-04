package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonTimePicker(
    time : LocalTime
) {
    val timePickerState = rememberTimePickerState(
        initialMinute = time.minute,
        initialHour = time.hour
    )


}
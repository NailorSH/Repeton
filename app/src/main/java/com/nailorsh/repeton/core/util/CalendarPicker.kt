package com.nailorsh.repeton.core.util

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import java.time.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    date: LocalDateTime,
    onDateChange: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val startOfDay = date.toLocalDate().atStartOfDay(ZoneId.systemDefault())
    val initSelDayInMills = startOfDay.toInstant().toEpochMilli().plus(startOfDay.offset.totalSeconds * 1000L)

    Log.d("DATE", Instant.ofEpochMilli(initSelDayInMills).toString())
    val state = rememberDatePickerState(
        yearRange = (2024..2050),
        initialSelectedDateMillis = initSelDayInMills
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            IconButton(
                onClick = {
                    onDateChange(
                        state.selectedDateMillis
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
        },
        dismissButton = {
            IconButton(
                onClick = onDismissRequest
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
    )
    {
        DatePicker(
            state = state,
        )
    }
}
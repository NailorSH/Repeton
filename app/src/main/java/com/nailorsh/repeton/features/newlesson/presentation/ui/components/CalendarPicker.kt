package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    date: LocalDate,
    onDateChange: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {



    val state = rememberDatePickerState(
        yearRange = (2024..2050),
        initialSelectedDateMillis = Calendar.getInstance().apply {
            set(Calendar.YEAR, date.year)
            set(Calendar.MONTH, date.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
        }.timeInMillis
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
        }
    )
    {
        DatePicker(state = state)
    }
}
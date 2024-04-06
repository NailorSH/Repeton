package com.nailorsh.repeton.features.newlesson.presentation.ui.components.first

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    date: LocalDateTime,
    onDateChange: (Long?) -> Unit,
    onDismissRequest: () -> Unit
) {



    val state = rememberDatePickerState(
        yearRange = (2024..2050),
        initialSelectedDateMillis = ZonedDateTime.of(date, ZoneId.systemDefault()).toInstant().toEpochMilli()
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
        DatePicker(
            state = state
        )
    }
}
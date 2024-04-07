package com.nailorsh.repeton.features.schedule.presentation.ui.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.Calendar

@Composable
fun CalendarDatePicker(
    selectedDay: LocalDate,
    selectedDayUpdate: (LocalDate) -> Unit,
    showDatePickerUpdate: () -> Unit,
    changeSelectionSource: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            changeSelectionSource()
            selectedDayUpdate(LocalDate.of(year, month + 1, dayOfMonth))
            showDatePickerUpdate()
        },
        selectedDay.year,
        selectedDay.monthValue - 1,
        selectedDay.dayOfMonth
    )


    // Минимальная дата доступная в календаре
    calendar.set(2024, Calendar.JANUARY, 1)
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.setOnDismissListener {
        showDatePickerUpdate()
    }

    datePickerDialog.show()
}
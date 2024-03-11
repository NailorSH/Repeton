package com.nailorsh.repeton.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.FakeLessonSource
import com.nailorsh.repeton.ui.theme.LineColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

@Composable
fun ScheduleScreen(
    onLessonClicked: (Int) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->

                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener {
            showDatePicker = false
        }

        datePickerDialog.show()

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Spacer(
            modifier = Modifier
                .height(dimensionResource(R.dimen.top_padding))
        )
        Divider(
            modifier = Modifier
                .width(dimensionResource(R.dimen.divider_width))
                .align(Alignment.CenterHorizontally),
            color = LineColor,
            thickness = 1.dp,

            )
        Box(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.padding_medium))
                .width(dimensionResource(R.dimen.schedule_screen_button_width))
                .height(63.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .align(Alignment.CenterHorizontally)
                .clickable {
                    showDatePicker = true
                }
        )
        {
            Text(
                text = stringResource(R.string.calendar),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .width(294.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Day("1", stringResource(R.string.mon))
            Day("2", stringResource(R.string.tue))
            Day("3", stringResource(R.string.wed))
            SelectedDay("4", stringResource(R.string.thu))
            Day("5", stringResource(R.string.fri))
            Day("6", stringResource(R.string.sat))
            Day("7", stringResource(R.string.sun))
        }
        Divider(
            modifier = Modifier
                .padding(top = 22.dp)
                .width(290.73.dp)
                .align(Alignment.CenterHorizontally),
            color = LineColor,
            thickness = 1.dp,

            )
        Column(
            modifier = Modifier
                .width(296.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (FakeLessonSource.loadLessons().isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 21.dp)
                        .width(dimensionResource(R.dimen.schedule_screen_button_width))
                        .height(95.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
                ) {
                    Text(
                        text = stringResource(R.string.lessons_not_found),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 13.dp, vertical = 16.dp)
                    )
                }
            } else {

                LessonBox(
                    lessonId = 0,
                    onClick = onLessonClicked
                )

                LessonBox(
                    lessonId = 1,
                    onClick = onLessonClicked
                )

                LessonBox(
                    lessonId = 2,
                    onClick = onLessonClicked
                )
            }

        }
        Button(
            onClick = { },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
                .width(dimensionResource(R.dimen.schedule_screen_button_width))
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.add_lesson_button),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun Day(number: String, day: String) {
    Box(
        modifier = Modifier
            .width(36.dp)
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(size = 8.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            Text(
                text = number,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = day,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.W400
            )
        }
    }
}


@Composable
fun SelectedDay(number: String, day: String) {
    Box(
        modifier = Modifier
            .width(36.dp)
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(size = 8.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            Text(
                text = number,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = day,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun LessonBox(lessonId: Int, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val lesson = FakeLessonSource.loadLessons()[lessonId]
    Box(
        modifier = Modifier
            .padding(top = 21.dp)
            .width(dimensionResource(R.dimen.schedule_screen_button_width))
            .height(95.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick(lessonId) }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 13.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = lesson.subject,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.headlineSmall,

                    )
                //Обрезала дату, при выборе конкретного дня она лишняя
                val startTimeCutted =
                    lesson.startTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        .substringAfter(", ")
                val endTimeCutted =
                    lesson.endTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        .substringAfter(", ")
                Text(
                    text = "$startTimeCutted - $endTimeCutted",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left
                )
            }
            Text(
                text = lesson.teacherName,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                letterSpacing = 0.25.sp,
            )
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ScheduleScreenPreview() {
    RepetonTheme {
        ScheduleScreen(
            onLessonClicked = { }
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ScheduleScreenPreviewDark() {
    RepetonTheme(darkTheme = true) {
        ScheduleScreen(
            onLessonClicked = { }
        )
    }
}
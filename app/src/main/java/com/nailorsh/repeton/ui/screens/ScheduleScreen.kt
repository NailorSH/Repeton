package com.nailorsh.repeton.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.FakeLessonSource
import com.nailorsh.repeton.ui.theme.AddLessonButtonColor
import com.nailorsh.repeton.ui.theme.LineColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import com.nailorsh.repeton.ui.theme.ScreenBackground
import com.nailorsh.repeton.ui.theme.SelectedDayColor
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
            .background(color = ScreenBackground)
    ) {
        Divider(
            modifier = Modifier
                .padding(top = 47.6.dp)
                .width(290.73.dp)
                .align(Alignment.CenterHorizontally),
            color = LineColor,
            thickness = 1.dp,

            )
        Box(
            modifier = Modifier
                .padding(top = 16.4.dp)
                .width(294.dp)
                .height(63.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
                .align(Alignment.CenterHorizontally)
                .clickable {
                    showDatePicker = true
                }
        )
        {
            Text(
                text = stringResource(R.string.calendar),
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
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
                        .width(296.dp)
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
                .width(298.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AddLessonButtonColor,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = stringResource(R.string.add_lesson_button),
                color = Color.White
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
                color = ScreenBackground,
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
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = day,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
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
                color = SelectedDayColor,
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
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = day,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun LessonBox(lessonId: Int, onClick: (Int) -> Unit) {
    val lesson = FakeLessonSource.loadLessons()[lessonId]
    Box(
        modifier = Modifier
            .padding(top = 21.dp)
            .width(296.dp)
            .height(95.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
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
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,

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
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = lesson.teacherName,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = lesson.title,
                style = LocalTextStyle.current.copy(
                    lineHeight = 13.sp
                ),
                color = Color.Black.copy(alpha = 0.6f),
                fontSize = 11.sp,
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
package com.nailorsh.repeton.ui.screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Calendar

@Composable
fun ScheduleScreen(
    onLessonClicked: (Int) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDay by remember {
        mutableStateOf(LocalDate.now())
    }
    if (showDatePicker) {
        val context = LocalContext.current
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedDay = LocalDate.of(year, month + 1, dayOfMonth)
                showDatePicker = false
            },
            selectedDay.year,
            selectedDay.monthValue - 1,
            selectedDay.dayOfMonth
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


        DaySlider(
            selectedDay = selectedDay,
            onDaySelected = { selectedDay = it },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )



        Divider(
            modifier = Modifier
                .padding(top = 22.dp)
                .width(dimensionResource(R.dimen.divider_width))
                .align(Alignment.CenterHorizontally),
            color = LineColor,
            thickness = dimensionResource(R.dimen.divider_thickness),

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

const val TAG = "SCHEDULE"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySlider(
    selectedDay: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.v(TAG, selectedDay.toString())

    val baseDate = LocalDate.of(1900, 1, 1)

    val initialPage = remember { ChronoUnit.WEEKS.between(baseDate, selectedDay).toInt() }

    var lastPage = remember { initialPage }

//    val startOfWeek = selectedWeek.minusDays(selectedDay.dayOfWeek.value.toLong() - 1)
//    val daysOfWeek = remember(selectedWeek) { List(7) { startOfWeek.plusDays(it.toLong()) } }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    LaunchedEffect(selectedDay) {
        val newPage = ChronoUnit.WEEKS.between(baseDate, selectedDay).toInt()
        if (pagerState.currentPage != newPage) {
            pagerState.scrollToPage(newPage)
        }
    }



    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .padding(top = 16.dp)
            .width(dimensionResource(R.dimen.schedule_screen_button_width)),

        ) { index ->
        Log.v(TAG, index.toString())
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            val weekStart = baseDate.plusWeeks(index.toLong())
            val daysOfWeek = List(7) { dayIndex -> weekStart.plusDays(dayIndex.toLong()) }

            items(daysOfWeek.size) { index ->
                Day(
                    day = daysOfWeek[index],
                    selected = daysOfWeek[index].equals(selectedDay)
                ) {
                    onDaySelected(daysOfWeek[index])
                }
            }
        }
    }



}

@Composable
fun Day(day: LocalDate, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer
    val textColor =
        if (selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiaryContainer
    val numberTextStyle = MaterialTheme.typography.headlineSmall
    val dayTextStyle = MaterialTheme.typography.labelSmall
    val dayFontWeight = FontWeight.W400

    Box(
        modifier = Modifier
            .width(36.dp)
            .height(48.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                color = textColor,
                textAlign = TextAlign.Center,
                style = numberTextStyle,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = when (day.dayOfWeek) {
                    DayOfWeek.MONDAY -> stringResource(R.string.mon)
                    DayOfWeek.TUESDAY -> stringResource(R.string.tue)
                    DayOfWeek.WEDNESDAY -> stringResource(R.string.wed)
                    DayOfWeek.THURSDAY -> stringResource(R.string.thu)
                    DayOfWeek.FRIDAY -> stringResource(R.string.fri)
                    DayOfWeek.SATURDAY -> stringResource(R.string.sat)
                    DayOfWeek.SUNDAY -> stringResource(R.string.sun)
                    else -> ""
                },
                color = textColor,
                textAlign = TextAlign.Center,
                style = dayTextStyle,
                fontWeight = dayFontWeight
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


                // Время начала и конца занятия
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
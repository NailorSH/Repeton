package com.nailorsh.repeton.ui.screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.theme.LineColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.Calendar

const val TAG = "SCHEDULE_SCREEN"
const val MAX_PAGE_COUNT = 10000
val BASE_DATE: LocalDate = LocalDate.of(2024, 1, 1)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    onLessonClicked: (Lesson) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDay by remember {
        mutableStateOf(LocalDate.now())
    }
    if (showDatePicker) {
        CalendarDatePicker(
            selectedDay = selectedDay,
            selectedDayUpdate = { selectedDay = it },
            showDatePickerUpdate = { showDatePicker = false }
        )

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
        HorizontalDivider(
            modifier = Modifier
                .width(dimensionResource(R.dimen.divider_width))
                .align(Alignment.CenterHorizontally),
            thickness = 1.dp,
            color = LineColor
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

        val initialPage = remember { ChronoUnit.DAYS.between(BASE_DATE, selectedDay).toInt() }
        val pagerState = rememberPagerState(
            initialPage = initialPage,
            pageCount = { MAX_PAGE_COUNT }
        )

        LaunchedEffect(pagerState.currentPage) {
            selectedDay = BASE_DATE.plusDays(pagerState.currentPage.toLong())
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {

                LessonsList(
                    onLessonClicked = onLessonClicked,
                    selectedDay = selectedDay,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.schedule_screen_button_width))
                        .align(Alignment.CenterHorizontally)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .width(dimensionResource(R.dimen.divider_width))
                        .align(Alignment.CenterHorizontally),

                    thickness = dimensionResource(R.dimen.divider_thickness),
                    color = LineColor
                )

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .width(dimensionResource(R.dimen.schedule_screen_button_width))
                        .height(52.dp)
                        .align(Alignment.CenterHorizontally),
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
    }
}

@Composable
fun LessonsList(
    onLessonClicked: (Lesson) -> Unit,
    selectedDay: LocalDate,
    modifier: Modifier = Modifier
) {

    /* TODO Подгрузка настоящих уроков */

    val lessons = remember { FakeLessonSource.loadLessons() }


    Column(
        modifier = modifier
    ) {
        if (lessons.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = 21.dp)
                    .width(dimensionResource(R.dimen.schedule_screen_button_width))
                    .height(dimensionResource(R.dimen.schedule_screen_lesson_size))
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Text(
                    text = stringResource(R.string.lessons_not_found),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 13.dp, vertical = 16.dp)
                )
            }
        } else {
            LazyColumn {
                items(lessons.size) {
                    /* TODO Убрать временное решение */
                    Log.v(TAG, lessons[it].toString())
                    if (lessons[it].startTime.dayOfYear == selectedDay.dayOfYear &&
                        lessons[it].startTime.year == selectedDay.year) {
                        LessonBox(
                            lesson = lessons[it],
                            onClick = onLessonClicked
                        )
                    }

                }
            }

        }

    }
}


@Composable
fun CalendarDatePicker(
    selectedDay: LocalDate,
    selectedDayUpdate: (LocalDate) -> Unit,
    showDatePickerUpdate: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySlider(
    selectedDay: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.v(TAG, selectedDay.toString())


    val initialPage = remember { ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt() }

    var lastPage = remember { initialPage }

//    val startOfWeek = selectedWeek.minusDays(selectedDay.dayOfWeek.value.toLong() - 1)
//    val daysOfWeek = remember(selectedWeek) { List(7) { startOfWeek.plusDays(it.toLong()) } }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { MAX_PAGE_COUNT }
    )

    LaunchedEffect(selectedDay) {
        val newPage = ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt()
        if (pagerState.currentPage != newPage) {
            pagerState.animateScrollToPage(newPage)
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
            val weekStart = BASE_DATE.plusWeeks(index.toLong())
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
fun LessonBox(lesson: Lesson, onClick: (Lesson) -> Unit, modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .padding(top = 21.dp)
            .width(dimensionResource(R.dimen.schedule_screen_button_width))
            .height(dimensionResource(R.dimen.schedule_screen_lesson_size))
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick(lesson) }
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
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 4.dp)
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

@Preview
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
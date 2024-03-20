package com.nailorsh.repeton.ui.screens

import CalendarDatePicker
import DaySlider
import LessonsList
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.repositories.FakeRepetonRepository
import com.nailorsh.repeton.domain.RepetonViewModel
import com.nailorsh.repeton.domain.ScheduleUiState
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.components.ErrorScreen
import com.nailorsh.repeton.ui.components.LoadingScreen
import com.nailorsh.repeton.ui.theme.LineColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val TAG = "SCHEDULE_SCREEN"
const val MAX_PAGE_COUNT = 10000
val BASE_DATE: LocalDate = LocalDate.of(2024, 1, 1)

private enum class SelectionSource {
    DayPager, DaySlider, Calendar, None
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    scheduleUiState: ScheduleUiState,
    getLessons: (LocalDate) -> Unit,
    onLessonClicked: (Lesson) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDay by remember {
        mutableStateOf(LocalDate.now())
    }

    val dayPagerState = rememberPagerState(
        initialPage = ChronoUnit.DAYS.between(BASE_DATE, selectedDay).toInt(),
        pageCount = { MAX_PAGE_COUNT }
    )

    val weekPagerState = rememberPagerState(
        initialPage = ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt(),
        pageCount = { MAX_PAGE_COUNT }
    )

    var selectionSource by remember { mutableStateOf(SelectionSource.None) }

    if (showDatePicker) {
        CalendarDatePicker(
            selectedDay = selectedDay,
            selectedDayUpdate = { selectedDay = it },
            showDatePickerUpdate = { showDatePicker = false },
            changeSelectionSource = { selectionSource = SelectionSource.Calendar }
        )
    }



    LaunchedEffect(dayPagerState.currentPage) {
        Log.v(TAG, dayPagerState.currentPage.toString())
        Log.v(TAG, selectionSource.toString())
        if (selectionSource == SelectionSource.None) {
            selectionSource = SelectionSource.DayPager
            selectedDay = BASE_DATE.plusDays(dayPagerState.currentPage.toLong())
        }

    }

    LaunchedEffect(selectedDay) {

        getLessons(selectedDay)
        Log.d(TAG, selectionSource.toString())
        when(selectionSource) {
            SelectionSource.DayPager -> {
                weekPagerState.animateScrollToPage(ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt())
            }

            SelectionSource.Calendar -> {
                dayPagerState.animateScrollToPage(ChronoUnit.DAYS.between(BASE_DATE, selectedDay).toInt())
                weekPagerState.animateScrollToPage(ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt())
            }

            SelectionSource.DaySlider -> {
                dayPagerState.animateScrollToPage(ChronoUnit.DAYS.between(BASE_DATE, selectedDay).toInt())
            }

            SelectionSource.None -> { }
        }
        selectionSource = SelectionSource.None
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
            changeSelectionSource = { selectionSource = SelectionSource.DaySlider },
            weekPagerState = weekPagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )



        HorizontalPager(
            state = dayPagerState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {

                // Отображение списка занятий
                when (scheduleUiState) {
                    is ScheduleUiState.Success -> {
                        LessonsList(
                            onLessonClicked = onLessonClicked,
                            lessons = scheduleUiState.lessons,
                            modifier = Modifier
                                .width(dimensionResource(R.dimen.schedule_screen_button_width))
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                    is ScheduleUiState.Loading -> {
                        LoadingScreen(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    else -> {
                        ErrorScreen(
                            retryAction = { getLessons(selectedDay) },
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }



                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .width(dimensionResource(R.dimen.divider_width))
                        .align(Alignment.CenterHorizontally),

                    thickness = dimensionResource(R.dimen.divider_thickness),
                    color = LineColor
                )

                Button(
                    onClick = { /* TODO Добавление нового занятия */ },
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



@Preview
@Composable
fun ScheduleScreenPreview() {
    RepetonTheme {
        ScheduleScreen(
            onLessonClicked = { },
            getLessons = { },
            scheduleUiState = RepetonViewModel(FakeRepetonRepository()).scheduleUiState
        )
    }
}
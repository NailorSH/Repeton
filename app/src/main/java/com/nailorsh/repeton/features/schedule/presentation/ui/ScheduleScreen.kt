package com.nailorsh.repeton.features.schedule.presentation.ui

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.core.ui.theme.LineColor
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.schedule.data.FakeScheduleRepository
import com.nailorsh.repeton.features.schedule.presentation.ui.components.CalendarDatePicker
import com.nailorsh.repeton.features.schedule.presentation.ui.components.DaySlider
import com.nailorsh.repeton.features.schedule.presentation.ui.components.LessonsList
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleUiState
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val TAG = "SCHEDULE_SCREEN"
const val MAX_PAGE_COUNT = 10000
val BASE_DATE: LocalDate = LocalDate.of(2024, 1, 1)

private enum class SelectionSource {
    DayPager, DaySlider, Calendar, None
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    scheduleUiState: ScheduleUiState,
    getLessons: () -> Unit,
    onLessonClicked: (Lesson) -> Unit,
    onNewLessonClicked: () -> Unit
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

    var lessonsMap: Map<LocalDate, List<Lesson>> = remember { emptyMap() }



    if (showDatePicker) {
        CalendarDatePicker(
            selectedDay = selectedDay,
            selectedDayUpdate = { selectedDay = it },
            showDatePickerUpdate = { showDatePicker = false },
            changeSelectionSource = { selectionSource = SelectionSource.Calendar }
        )
    }



    LaunchedEffect(dayPagerState.currentPage) {
        if (selectionSource == SelectionSource.None || selectionSource == SelectionSource.DayPager) {
            selectionSource = SelectionSource.DayPager
            selectedDay = BASE_DATE.plusDays(dayPagerState.currentPage.toLong())
        }

    }

    LaunchedEffect(selectedDay) {


        when (selectionSource) {
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

            SelectionSource.None -> {}
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
                .fillMaxWidth()
                .clickable {
                    getLessons()
                }
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





        when (scheduleUiState) {
            is ScheduleUiState.Success -> {
                lessonsMap = scheduleUiState.lessons

                DaySlider(
                    selectedDay = selectedDay,
                    onDaySelected = { selectedDay = it },
                    changeSelectionSource = { selectionSource = SelectionSource.DaySlider },
                    weekPagerState = weekPagerState,
                    lessonsMap = lessonsMap,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                HorizontalPager(
                    state = dayPagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ) {

                        // Отображение списка занятий
                        val day = BASE_DATE.plusDays(page.toLong())
                        LessonsList(
                            onLessonClicked = onLessonClicked,
                            lessons = lessonsMap.getOrDefault(day, emptyList()),
                            modifier = Modifier
                                .width(dimensionResource(R.dimen.schedule_screen_button_width))
                                .weight(0.7f)
                                .align(Alignment.CenterHorizontally)
                        )

                        Column(
                            modifier = Modifier
                                .weight(0.3f)
                                .align(Alignment.CenterHorizontally)
                        ) {

                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(top = 22.dp)
                                    .width(dimensionResource(R.dimen.divider_width))
                                    .align(Alignment.CenterHorizontally),

                                thickness = dimensionResource(R.dimen.divider_thickness),
                                color = LineColor
                            )

                            Button(
                                onClick = { onNewLessonClicked() },
                                modifier = Modifier
                                    .padding(top = 32.dp)
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
                                )
                            }
                        }

                    }
                }
            }

            is ScheduleUiState.Loading -> {
                LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            else -> {
                ErrorScreen(
                    retryAction = { getLessons() },
                    modifier = Modifier
                        .fillMaxSize()
                )
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
            scheduleUiState = ScheduleViewModel(FakeScheduleRepository()).scheduleUiState,
            onNewLessonClicked = { }
        )
    }
}
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.core.ui.theme.LineColor
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.schedule.presentation.ui.components.CalendarDatePicker
import com.nailorsh.repeton.features.schedule.presentation.ui.components.DaySlider
import com.nailorsh.repeton.features.schedule.presentation.ui.components.LessonsList
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleAction
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleState
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleUIEvent
import com.nailorsh.repeton.features.schedule.presentation.viewmodel.ScheduleUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val TAG = "SCHEDULE_SCREEN"
const val MAX_PAGE_COUNT = 10000
val BASE_DATE: LocalDate = LocalDate.of(2024, 1, 1)

private enum class SelectionSource {
    DayPager, DaySlider, Calendar, None
}

@Composable
fun ScheduleScreen(
    scheduleUiState: ScheduleUiState,
    uiEvents : Flow<ScheduleUIEvent>,
    onAction: (ScheduleAction) -> Unit
) {
    when (scheduleUiState) {
        ScheduleUiState.Loading ->
            LoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
            )

        ScheduleUiState.Error -> ErrorScreen(
            retryAction = { onAction(ScheduleAction.RetryAction) },
            modifier = Modifier
                .fillMaxSize()
        )

        is ScheduleUiState.Success -> ScheduleScreenContent(
            scheduleState = scheduleUiState.scheduleState,
            uiEvents = uiEvents,
            onAction = onAction
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreenContent(
    scheduleState: ScheduleState,
    uiEvents: Flow<ScheduleUIEvent>,
    onAction: (ScheduleAction) -> Unit
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


    val snackbarHostState = remember { SnackbarHostState() }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvents.collect { uiEvent ->
                snackbarHostState.showSnackbar(
                    message = context.getString(uiEvent.msg),
                    withDismissAction = true
                )
            }
        }
    }


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
                weekPagerState.animateScrollToPage(
                    ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt()
                )
            }

            SelectionSource.Calendar -> {
                dayPagerState.animateScrollToPage(
                    ChronoUnit.DAYS.between(BASE_DATE, selectedDay).toInt()
                )
                weekPagerState.animateScrollToPage(
                    ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt()
                )
            }

            SelectionSource.DaySlider -> {
                dayPagerState.animateScrollToPage(
                    ChronoUnit.DAYS.between(BASE_DATE, selectedDay).toInt()
                )
            }

            SelectionSource.None -> {}
        }
        selectionSource = SelectionSource.None
    }


    val pullToRefreshState = rememberPullToRefreshState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {

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
                lessonsMap = scheduleState.lessons,
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
                        onLessonClicked = { onAction(ScheduleAction.NavigateToLesson(it)) },
                        lessons = scheduleState.lessons.getOrDefault(day, emptyList()),
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.schedule_screen_button_width))
                            .weight(0.7f)
                            .align(Alignment.CenterHorizontally)
                    )
                    if (scheduleState.isTutor) {
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
                                onClick = { onAction(ScheduleAction.NavigateToNewLesson) },
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
        }

        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))


        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(key1 = true) {
                onAction(ScheduleAction.Refresh)
            }
        }
        LaunchedEffect(key1 = scheduleState.isRefreshing) {
            if (scheduleState.isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}


@Preview
@Composable
fun ScheduleScreenPreview() {
    RepetonTheme {
        ScheduleScreen(
            scheduleUiState = ScheduleUiState.Success(ScheduleState()),
            onAction = { },
            uiEvents = flowOf()
        )
    }
}
package com.nailorsh.repeton.schedule.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.currentlesson.data.model.Lesson
import com.nailorsh.repeton.schedule.presentation.ui.BASE_DATE
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySlider(
    selectedDay: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    weekPagerState: PagerState,
    changeSelectionSource: () -> Unit,
    lessonsMap: Map<LocalDate, List<Lesson>>,
    modifier: Modifier = Modifier
) {



    HorizontalPager(
        state = weekPagerState,
        pageSpacing = dimensionResource(R.dimen.schedule_screen_days_padding),
        modifier = modifier
            .padding(top = 16.dp)
            .width(dimensionResource(R.dimen.schedule_screen_button_width)),
    ) { index ->
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.schedule_screen_days_padding))
        ) {
            val weekStart = BASE_DATE.plusWeeks(index.toLong())
            val daysOfWeek = List(7) { dayIndex -> weekStart.plusDays(dayIndex.toLong()) }

            items(daysOfWeek.size) { index ->
                Day(
                    day = daysOfWeek[index],
                    selectedDay = selectedDay,
                    hasLessons = lessonsMap[daysOfWeek[index]] != null,
                ) {
                    changeSelectionSource()
                    onDaySelected(daysOfWeek[index])
                }
            }
        }
    }


}
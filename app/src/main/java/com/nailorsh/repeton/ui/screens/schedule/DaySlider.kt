import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.screens.BASE_DATE
import com.nailorsh.repeton.ui.screens.Day
import com.nailorsh.repeton.ui.screens.MAX_PAGE_COUNT
import com.nailorsh.repeton.ui.screens.TAG
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySlider(
    selectedDay: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.v(TAG, selectedDay.toString())


    val initialPage = remember { ChronoUnit.WEEKS.between(BASE_DATE, selectedDay).toInt() }

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
        pageSpacing = dimensionResource(R.dimen.schedule_screen_days_padding),
        modifier = modifier
            .padding(top = 16.dp)
            .width(dimensionResource(R.dimen.schedule_screen_button_width)),
    ) { index ->
        Log.v(TAG, index.toString())
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.schedule_screen_days_padding))
        ) {
            val weekStart = BASE_DATE.plusWeeks(index.toLong())
            val daysOfWeek = List(7) { dayIndex -> weekStart.plusDays(dayIndex.toLong()) }

            items(daysOfWeek.size) { index ->
                Day(
                    day = daysOfWeek[index],
                    selectedDay = selectedDay,
                ) {
                    onDaySelected(daysOfWeek[index])
                }
            }
        }
    }


}
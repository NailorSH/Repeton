import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.screens.LessonBox

@Composable
fun LessonsList(
    onLessonClicked: (Lesson) -> Unit,
    lessons: List<Lesson>,
    modifier: Modifier = Modifier
) {


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
                    LessonBox(
                        lesson = lessons[it],
                        onClick = onLessonClicked
                    )
                }

            }
        }

    }

}
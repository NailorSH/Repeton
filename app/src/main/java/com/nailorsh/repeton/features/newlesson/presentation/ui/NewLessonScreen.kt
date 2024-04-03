package com.nailorsh.repeton.features.newlesson.presentation.ui

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.VisualTransformation
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Lesson
import com.nailorsh.repeton.core.ui.components.RepetonDivider
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.newlesson.data.FakeNewLessonRepository
import com.nailorsh.repeton.features.newlesson.data.NewLessonRepository
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.SubjectTextField
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel
import java.time.LocalDateTime

@Composable
fun NewLessonScreen(
    newLessonUiState: NewLessonUiState,
    onNavigateBack: () -> Unit,
    onSaveLessons: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    var lesson = remember {
        Lesson(
            id = 0,
            subject = "a",
            title = "s",
            additionalMaterials = "",
            description = "",
            homeworkLink = "",
            teacherName = "lol",
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now()
        )
    }

    var subject by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(LocalDateTime.now()) }
    var endTime by remember { mutableStateOf(startTime.plusMinutes(90)) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceBright)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            modifier = Modifier
                .width(dimensionResource(R.dimen.schedule_screen_button_width))
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = stringResource(R.string.new_lesson_screen_headline),
                    letterSpacing = 0.5.sp,
                )
            }

        }
        RepetonDivider(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = dimensionResource(R.dimen.padding_medium))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {

            SubjectTextField(
                subject = subject,
                onSubjectChange = { subject = it },
                newLessonUiState = newLessonUiState,
                expanded = expanded,
                onExpandedChange = { expanded = it }
            )
        }

    }
}


@Preview
@Composable
fun NewLessonScreenPreview() {
    RepetonTheme {
        NewLessonScreen(
            newLessonUiState = NewLessonViewModel(FakeNewLessonRepository()).newLessonUiState,
            onNavigateBack = {},
            onSaveLessons = {},
        )
    }
}
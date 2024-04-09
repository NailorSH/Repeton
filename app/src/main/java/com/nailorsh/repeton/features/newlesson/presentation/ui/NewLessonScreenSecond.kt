package com.nailorsh.repeton.features.newlesson.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Attachment
import com.nailorsh.repeton.common.data.models.Homework
import com.nailorsh.repeton.core.util.cameraRequest
import com.nailorsh.repeton.core.util.fileAttachmentRequest
import com.nailorsh.repeton.features.newlesson.data.FakeNewLessonRepository
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.NewLessonTopBar
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.AdditionalMaterialsTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.DescriptionTextField
import com.nailorsh.repeton.features.newlesson.presentation.ui.components.second.HomeworkTextField
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonSecondUiState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonState
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonViewModel


@Composable
fun NewLessonScreenSecond(
    lessonState: NewLessonState,
    onNavigateBack: () -> Unit,
    onNavigateSuccessfulSave: () -> Unit,
    onSaveLesson: (String?, Homework?, String?) -> Unit,
    modifier: Modifier = Modifier
) {


    var description by remember { mutableStateOf(lessonState.description ?: "") }
    var homeworkText by remember { mutableStateOf(lessonState.homework?.text ?: "") }
    var additionalMaterials by remember { mutableStateOf(lessonState.additionalMaterials ?: "") }
    var homeworkAttachments by remember { mutableStateOf(lessonState.homework?.attachments ?: emptyList()) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    LaunchedEffect(lessonState.secondUiState) {
        when (lessonState.secondUiState) {
            NewLessonSecondUiState.Saved -> onNavigateSuccessfulSave()
            else -> {}
        }
    }


    val onClickAddPhoto = cameraRequest(
        onImageCaptured = { /* TODO Обработка изображения и выгрузка в локальный репозиторий */ },
        onPermissionDenied = { Toast.makeText(context, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show() }
    )

    val onClickAttachFile = fileAttachmentRequest(
        onFileChosen = { /* TODO Обработка файла и выгрузка в локальный репозиторий */ },
        onFileChoosingFailed = {
            Toast.makeText(
                context,
                R.string.file_selection_failed, Toast.LENGTH_SHORT
            ).show()
        }
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceBright)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            )
    ) {

        NewLessonTopBar(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {

            // Поле добавления описания
            DescriptionTextField(
                description = description,
                onDescriptionChange = { description = it }
            )
            // Поле добавления домашнего задания
            HomeworkTextField(
                homeworkText = homeworkText,
                onHomeworkTextChange = { homeworkText = it },
                onClickAddAPhoto = onClickAddPhoto,
                onClickAttachFile = onClickAttachFile
            )

            // Поле добавления дополнительных материалов
            AdditionalMaterialsTextField(
                additionalMaterials = additionalMaterials,
                onAdditionalMaterialsChange = { additionalMaterials = it }
            )


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Button(

                    onClick = {
                        onSaveLesson(
                            if (description != "") description else null,
                            if (homeworkText != "" || homeworkAttachments.isNotEmpty()) Homework(
                                homeworkText,
                                homeworkAttachments
                            ) else null,
                            if (additionalMaterials != "") additionalMaterials else null
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = stringResource(R.string.new_lesson_screen_save_new_lesson),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NewLessonScreenSecondPreview() {

    NewLessonScreenSecond(
        lessonState = NewLessonViewModel(FakeNewLessonRepository()).state.collectAsState().value,
        onNavigateBack = {},
        onSaveLesson = { a, b, c -> },
        onNavigateSuccessfulSave = {}
    )

}
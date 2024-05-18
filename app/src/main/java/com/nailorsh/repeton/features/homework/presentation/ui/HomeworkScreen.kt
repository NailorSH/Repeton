package com.nailorsh.repeton.features.homework.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkBottomBar
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkImages
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkTextCard
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkTopBar
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkAction
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkState
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkUiState

@Composable
fun HomeworkScreen(
    uiState: HomeworkUiState,
    onAction: (HomeworkAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            HomeworkUiState.Error -> Text("ERRROR")
            HomeworkUiState.Loading -> CircularProgressIndicator()
            is HomeworkUiState.Success -> HomeworkScreenContent(
                state = uiState.state,
                onAction = onAction
            )
        }
    }

}

@Composable
fun HomeworkScreenContent(
    state: HomeworkState,
    onAction: (HomeworkAction) -> Unit
) {
    Scaffold(
        topBar = {
            HomeworkTopBar(
                title = state.homework.title,
                onNavigateBack = {
                    onAction(HomeworkAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            HomeworkBottomBar(
                answerText = state.answerText,
                onTextChange = { onAction(HomeworkAction.UpdateAnswerText(it)) }
            )
        },
        modifier = Modifier.imePadding()
    ) { paddingValues ->

        if (state.showImageDialogue) {
            Dialog(onDismissRequest = { onAction(HomeworkAction.HideImageDialogue) }) {

                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.imageToShowInDialogue?.url)
                        .build(),
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .heightIn(480.dp)
                        .widthIn(280.dp)
                )

            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                HomeworkTextCard(
                    homeworkText = state.homework.description,
                    author = state.homework.author
                )
            }
            if (state.homework.images.isNotEmpty()) {
                item {
                    HomeworkImages(
                        imageList = state.homework.images,
                        onImageClick = { onAction(HomeworkAction.ShowImageDialogue(it)) }
                    )
                }
            }
        }

    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun HomeworkScreenPreview() {
//    HomeworkScreen(onAction = { })
//}
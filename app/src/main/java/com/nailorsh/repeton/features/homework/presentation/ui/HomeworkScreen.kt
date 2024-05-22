package com.nailorsh.repeton.features.homework.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.core.ui.components.images.ImageSliderDialogue
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkBottomBar
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkImages
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkTextCard
import com.nailorsh.repeton.features.homework.presentation.ui.components.HomeworkTopBar
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkAction
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkState
import com.nailorsh.repeton.features.homework.presentation.viewmodel.HomeworkUiState
import kotlinx.coroutines.launch

@Composable
fun HomeworkScreen(
    uiState: HomeworkUiState,
    onAction: (HomeworkAction) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is HomeworkUiState.Error -> Text(uiState.errorMsg)
            HomeworkUiState.Loading -> CircularProgressIndicator()
            is HomeworkUiState.Success -> HomeworkScreenContent(
                state = uiState.state,
                onAction = onAction
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeworkScreenContent(
    state: HomeworkState,
    onAction: (HomeworkAction) -> Unit
) {
    val pagerSliderState =
        rememberPagerState(initialPage = 0, pageCount = { state.homework.images.size })
    val pagerDialogueState =
        rememberPagerState(initialPage = 0, pageCount = { state.homework.images.size })
    val coroutineScope = rememberCoroutineScope()
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
                onTextChange = { onAction(HomeworkAction.UpdateAnswerText(it)) },
                onSendMessage = { onAction(HomeworkAction.SendMessage) }
            )
        },
        modifier = Modifier.imePadding()
    ) { paddingValues ->
        LaunchedEffect(key1 = pagerDialogueState.currentPage) {
            pagerSliderState.scrollToPage(pagerDialogueState.currentPage)
        }
        if (state.showImageDialogue) {
            ImageSliderDialogue(
                onDismissRequest = {
                    onAction(HomeworkAction.HideImageDialogue)
                },
                images = state.homework.images,
                pagerState = pagerDialogueState
            )

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
                        onImageClick = {
                            onAction(HomeworkAction.ShowImageDialogue(it))
                            coroutineScope.launch {
                                pagerDialogueState.scrollToPage(pagerSliderState.currentPage)
                            }
                        },
                        coroutineScope = coroutineScope,
                        pagerState = pagerSliderState
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
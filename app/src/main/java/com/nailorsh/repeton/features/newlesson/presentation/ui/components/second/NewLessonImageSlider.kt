package com.nailorsh.repeton.features.newlesson.presentation.ui.components.second

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.core.ui.components.images.ImageSlider
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewLessonImageSlider(
    imageList: List<Attachment.Image>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onImageClick: (Attachment.Image) -> Unit,
    onDeleteImage: (Int) -> Unit,
    updateImageDescription: (String, Int) -> Unit
) {
    ImageSlider(
        imageList = imageList,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        onImageClick = onImageClick,
    ) {
        IconButton(onClick = { onDeleteImage(pagerState.currentPage) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cancel_w400),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
    ImageDescriptionTextField(
        description = imageList[pagerState.currentPage].description ?: "",
        onDescriptionChange = { updateImageDescription(it, pagerState.currentPage) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
    )
}


package com.nailorsh.repeton.core.ui.components.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.nailorsh.repeton.common.data.models.lesson.Attachment

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSliderDialogue(
    onDismissRequest: () -> Unit,
    images: List<Attachment.Image>,
    pagerState: PagerState,
) {
    Dialog(onDismissRequest = onDismissRequest) {

        HorizontalPager(state = pagerState, pageSpacing = 24.dp) { page ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                SubcomposeAsyncImage(
                    model = images[page].url,
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }


        }
    }
}
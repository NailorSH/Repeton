package com.nailorsh.repeton.features.homework.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.common.data.models.lesson.Attachment
import com.nailorsh.repeton.core.ui.components.images.ImageSlider
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeworkImages(
    imageList: List<Attachment.Image>,
    onImageClick: (Attachment.Image) -> Unit,
    coroutineScope : CoroutineScope,
    pagerState: PagerState,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Text("Изображения", style = MaterialTheme.typography.titleMedium)

        ImageSlider(
            imageList = imageList,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            onImageClick = onImageClick
        )
        if (imageList[pagerState.currentPage].description != null) {
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
                    Text(
                        text = imageList[pagerState.currentPage].description!!,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

    }
}


//@Preview
//@Composable
//fun HomeworkImagesPreview() {
//    HomeworkImages()
//}
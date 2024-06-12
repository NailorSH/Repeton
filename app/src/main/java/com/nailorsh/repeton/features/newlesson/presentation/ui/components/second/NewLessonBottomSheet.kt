package com.nailorsh.repeton.features.newlesson.presentation.ui.components.second

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLessonBottomSheet(
    sheetState: SheetState,
    cameraRequest: () -> Unit,
    photoPickerRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.height(160.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable {
                        cameraRequest()
                    }
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .weight(0.5f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_a_photo_w300),
                    contentDescription = null
                )
                Text(
                    text = "Сделать фото", style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable { photoPickerRequest() }
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .weight(0.5f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_photo_library),
                    contentDescription = null
                )
                Text(text = "Выбрать фото", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewLessonBottomSheetPreview() {
    NewLessonBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        cameraRequest = { /*TODO*/ },
        photoPickerRequest = { }
    ) {

    }


}
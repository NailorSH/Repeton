package com.nailorsh.repeton.features.about.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@Composable
fun AboutBottomSheet(
    text: String,
    onUpdateAbout: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var aboutNew by remember {
        mutableStateOf(text)
    }
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(horizontal = 24.dp)) {


        TextField(
            value = aboutNew,
            onValueChange = { aboutNew = it },
            modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("Не более 200 символов")},
            label = {
                Text(
                    text = stringResource(
                        id = R.string.about_screen_bio
                    )
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.about_screen_bio_placeholder)
                )
            },
            leadingIcon = {
                IconButton(onClick = { aboutNew = "" }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel_w400),
                        contentDescription = null
                    )
                }
            }
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            ElevatedButton(onClick = onDismiss, modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 32.dp)) {
                Text("Отмена")
            }
            ElevatedButton(onClick = { onUpdateAbout(aboutNew) }, modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 32.dp)) {
                Text("Сохранить")
            }
        }
        Spacer(modifier = Modifier)
    }
}

@Preview
@Composable
fun AboutBottomSheetPreview() {
    AboutBottomSheet("", onUpdateAbout = {}) {

    }
}
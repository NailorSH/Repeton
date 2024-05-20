package com.nailorsh.repeton.features.homework.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R

@Composable
fun HomeworkBottomBar(
    answerText : String,
    onSendMessage : () -> Unit,
    onTextChange : (String) -> Unit,
) {
    BottomAppBar(
        containerColor = Color.Transparent,
        contentPadding = BottomAppBarDefaults.ContentPadding,
        modifier = Modifier.wrapContentSize()
    ) {
        OutlinedTextField(
            value = answerText,
            onValueChange = onTextChange,
            placeholder = { Text(stringResource(R.string.homework_screen_answer_placeholder)) },
            leadingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.ic_attach_file_w300), contentDescription = null)
                }
            },
            trailingIcon = {
                if (answerText == "") {
                    Row() {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.ic_add_a_photo_w300), contentDescription = null)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.ic_photo_library), contentDescription = null)
                        }
                    }
                } else {
                    IconButton(onClick = onSendMessage) {
                        Icon(painter = painterResource(id = R.drawable.ic_send), contentDescription = null)
                    }
                }
                
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
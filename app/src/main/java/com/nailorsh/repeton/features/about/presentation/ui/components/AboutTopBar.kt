package com.nailorsh.repeton.features.about.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.LessonTopBar

@Composable
fun AboutTopBar(
    isExpanded: Boolean,
    showChangeOptions: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onChangePhoto: () -> Unit,
    onChangeAbout: () -> Unit,
    onSaveChanges : () -> Unit,
) {
    LessonTopBar(onNavigateBack = onNavigateBack, title = "О себе") {
        IconButton(onClick = { onSaveChanges() }) {
            Icon(imageVector = Icons.Default.Done, contentDescription = null)
        }
        IconButton(onClick = { showChangeOptions(true) }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
        DropdownMenu(expanded = isExpanded, onDismissRequest = { showChangeOptions(false) }) {
            DropdownMenuItem(
                text = { Text("Изменить биографию") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null
                    )
                }, onClick = {
                    onChangeAbout()
                    showChangeOptions(false)
                })
            DropdownMenuItem(
                text = { Text("Изменить фотографию") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_a_photo_w300),
                        contentDescription = null
                    )
                },
                onClick = {
                    onChangePhoto()
                    showChangeOptions(false)
                })
        }
    }
}
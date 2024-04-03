package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.features.newlesson.presentation.viewmodel.NewLessonUiState

@Composable
fun SubjectTextField(
    subject: String,
    onSubjectChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    newLessonUiState: NewLessonUiState,
) {
    Column {
        OutlinedTextField(
            singleLine = true,
            value = subject,
            onValueChange = {
                onSubjectChange(it)
                onExpandedChange(true)
            },
            label = { Text(stringResource(R.string.new_lesson_screen_subject_label)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_subject)
                )
            },
            placeholder = { Text(stringResource(R.string.new_lesson_screen_subject_placeholder)) },
            supportingText = { Text(stringResource(R.string.required_field)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = { onExpandedChange(!expanded) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Список предметов"
                    )
                }
            }
        )

        val subjects = when (newLessonUiState) {
            NewLessonUiState.Error -> listOf()
            NewLessonUiState.Loading -> listOf()
            is NewLessonUiState.Success -> newLessonUiState.subjects
        }

        AnimatedVisibility(visible = expanded) {

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                properties = PopupProperties(
                    focusable = false,
                )

            ) {
                subjects.filter {
                    it.lowercase().startsWith(subject.lowercase())
                }.sorted().forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            onSubjectChange(it)
                            onExpandedChange(false)
                        }
                    )
                }
            }

        }


    }


}


package com.nailorsh.repeton.features.newlesson.presentation.ui.components.first

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectTextField(
    subject: String,
    onSubjectChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    subjects: List<Subject>,
    isError: Boolean,
) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            isError = isError,
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
                AnimatedVisibility(
                    visible = !expanded,
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally()
                ) {
                    IconButton(
                        onClick = { onExpandedChange(true) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.new_lesson_screen_show_subject_list)
                        )
                    }
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally()
                ) {
                    IconButton(
                        onClick = { onExpandedChange(false) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = stringResource(R.string.new_lesson_screen_hide_subject_list)
                        )
                    }
                }
            },
            modifier = Modifier.menuAnchor()
        )

        if (subjects.isNotEmpty()) {

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                subjects.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.subjectName) },
                        onClick = {
                            onSubjectChange(it.subjectName)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

    }


}


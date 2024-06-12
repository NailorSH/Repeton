package com.nailorsh.repeton.features.students.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LessonTopBar
import com.nailorsh.repeton.core.ui.components.LoadingDialogue
import com.nailorsh.repeton.core.ui.components.UserImage
import com.nailorsh.repeton.features.students.presentation.ui.components.StudentItem
import com.nailorsh.repeton.features.students.presentation.viewmodel.StudentsAction
import com.nailorsh.repeton.features.students.presentation.viewmodel.StudentsState
import com.nailorsh.repeton.features.students.presentation.viewmodel.StudentsUIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    state: StudentsState,
    uiEvents: Flow<StudentsUIEvent>,
    onAction: (StudentsAction) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var active by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvents.collect { uiEvent ->
                snackbarHostState.showSnackbar(
                    message = context.getString(uiEvent.msg),
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LessonTopBar(
                onNavigateBack = { onAction(StudentsAction.NavigateBack) },
                title = stringResource(R.string.students_screen_title)
            ) {
                IconButton(onClick = { active = true }) {
                    Icon(
                        painterResource(id = R.drawable.ic_person_add),
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(
                visible = active,
                enter = slideInVertically { it } + expandVertically(
                    expandFrom = Alignment.Bottom
                ),
            ) {
                SearchBar(
                    query = state.query,
                    onQueryChange = { onAction(StudentsAction.QueryChange(it)) },
                    onSearch = { onAction(StudentsAction.Search) },
                    active = active,
                    onActiveChange = { active = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.students_screen_search_placeholder)) },
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    trailingIcon = {
                        IconButton(onClick = { onAction(StudentsAction.Search) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = { active = false }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_upward),
                                contentDescription = null
                            )
                        }
                    }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.studentsList) { student ->
                            ListItem(
                                leadingContent = {
                                    UserImage(photoSrc = student.photoSrc, 48.dp)
                                },
                                headlineContent = { Text("${student.surname} ${student.name}") },
                                trailingContent = {
                                    IconButton(onClick = {
                                        onAction(StudentsAction.AddStudent(student))
                                        active = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null
                                        )
                                    }
                                }, modifier = Modifier.clickable {
                                    onAction(StudentsAction.AddStudent(student))
                                    active = false
                                })
                        }
                    }
                }
            }

        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        } else if (state.error) {
            ErrorScreen(retryAction = { onAction(StudentsAction.RetryAction) })
        } else {
            when (val student = state.studentAction) {
                is StudentsAction.AddStudent -> AlertDialog(
                    title = { Text(stringResource(R.string.students_screen_add_student_title)) },
                    text = {
                        Text(
                            stringResource(
                                R.string.students_screen_add_student_text,
                                student.student.surname,
                                student.student.name
                            )
                        )
                    },
                    onDismissRequest = { onAction(StudentsAction.DismissAlertDialog) },
                    dismissButton = {
                        TextButton(onClick = { onAction(StudentsAction.DismissAlertDialog) }) {
                            Text(stringResource(R.string.dismiss))
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { onAction(StudentsAction.ConfirmAlertDialog) }) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                )

                is StudentsAction.RemoveStudent -> AlertDialog(
                    text = {
                        Text(
                            stringResource(
                                R.string.students_screen_remove_student_text,
                                student.student.surname,
                                student.student.name
                            )
                        )
                    },
                    title = { Text(stringResource(R.string.students_screen_remove_student_title)) },
                    onDismissRequest = { onAction(StudentsAction.DismissAlertDialog) },
                    dismissButton = {
                        TextButton(onClick = { onAction(StudentsAction.DismissAlertDialog) }) {
                            Text(stringResource(R.string.dismiss))
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { onAction(StudentsAction.ConfirmAlertDialog) }) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                )

                null -> {
                }
            }

            if (state.showLoadingScreen) {
                LoadingDialogue()
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            focusManager.clearFocus()
                        }
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.userStudents) { student ->
                    StudentItem(
                        student = student,
                        onRemoveStudent = {
                            onAction(
                                StudentsAction.RemoveStudent(student)
                            )
                        }
                    )
                }
            }
        }
    }

}


@Preview
@Composable
fun SubjectsScreenPreview() {
    StudentsScreen(state = StudentsState(isLoading = false), uiEvents = flow { }, {})
}
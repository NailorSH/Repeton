package com.nailorsh.repeton.features.subjects.presentation.ui

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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
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
import com.nailorsh.repeton.features.subjects.presentation.ui.components.SubjectItem
import com.nailorsh.repeton.features.subjects.presentation.viewmodel.SubjectsAction
import com.nailorsh.repeton.features.subjects.presentation.viewmodel.SubjectsState
import com.nailorsh.repeton.features.subjects.presentation.viewmodel.SubjectsUIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    state: SubjectsState,
    uiEvents: Flow<SubjectsUIEvent>,
    onAction: (SubjectsAction) -> Unit
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
                onNavigateBack = { onAction(SubjectsAction.NavigateBack) },
                title = stringResource(R.string.subject_screen_title)
            ) {
                IconButton(onClick = { onAction(SubjectsAction.SaveUpdates) }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
                IconButton(onClick = { active = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
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
                    onQueryChange = { onAction(SubjectsAction.QueryChange(it)) },
                    onSearch = { onAction(SubjectsAction.Search) },
                    active = active,
                    onActiveChange = { active = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.subject_screen_search_placeholder)) },
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    trailingIcon = {
                        IconButton(onClick = { onAction(SubjectsAction.Search) }) {
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
                        items(state.subjectsList) { subject ->
                            ListItem(headlineContent = { Text(subject.name) }, leadingContent = {
                                IconButton(onClick = {
                                    onAction(SubjectsAction.AddSubject(subject))
                                    active = false
                                }) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                }
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
            ErrorScreen(retryAction = { onAction(SubjectsAction.RetryAction) })
        } else {
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
                items(state.userSubjects) { subject ->
                    SubjectItem(
                        subject = subject,
                        price = subject.price.toString(),
                        onPriceSave = { subjectWithPrice, price ->
                            onAction(
                                SubjectsAction.SaveSubjectPrice(
                                    subjectWithPrice,
                                    price
                                )
                            )
                        },
                        onRemoveSubject = { onAction(SubjectsAction.RemoveSubject(subject)) })
                }
            }
        }
    }

}


@Preview
@Composable
fun SubjectsScreenPreview() {
    SubjectsScreen(state = SubjectsState(isLoading = false), uiEvents = flow { }, {})
}
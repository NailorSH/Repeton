package com.nailorsh.repeton.features.tutorsearch.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.tutorsearch.presentation.ui.components.TutorList
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchAction
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchState
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.TutorSearchUIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: TutorSearchState,
    uiEvents: Flow<TutorSearchUIEvent>,
    onAction: (TutorSearchAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

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
        topBar = {
            SearchBar(
                query = state.query,
                onQueryChange = { onAction(TutorSearchAction.QueryChange(it)) },
                onSearch = { onAction(TutorSearchAction.Search) },
                active = state.active,
                onActiveChange = { onAction(TutorSearchAction.ActiveChange(it)) },
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                trailingIcon = {
                    if (state.active) {
                        IconButton(onClick = { onAction(TutorSearchAction.Clear) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            ) {}
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        } else if (state.error) {
            ErrorScreen(
                retryAction = { onAction(TutorSearchAction.RetryAction) },
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        } else {
            TutorList(
                tutors = state.tutorsList,
                onTutorCardClicked = { onAction(TutorSearchAction.NavigateToTutorProfile(it)) },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun SearchScreenPreview() {
    RepetonTheme {
        SearchScreen(
            state = TutorSearchState(isLoading = false), uiEvents = flow { }, {}
        )
    }
}
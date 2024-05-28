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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.tutorsearch.presentation.ui.components.TutorList
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    typingGetSearchResults: (String) -> Unit,
    getSearchResults: () -> Unit,
    searchUiState: SearchUiState,
    onTutorCardClicked: (Id) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    var active by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        getSearchResults()
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    typingGetSearchResults(it)
                },
                onSearch = { getSearchResults() },
                active = active,
                onActiveChange = { active = it },
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                trailingIcon = {
                    if (active) {
                        IconButton(onClick = { query = "" }) {
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
        when (searchUiState) {
            is SearchUiState.None -> {}
            is SearchUiState.Loading -> LoadingScreen(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )

            is SearchUiState.Success -> TutorList(
                tutors = searchUiState.tutors,
                onTutorCardClicked = onTutorCardClicked,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )

            is SearchUiState.Error -> ErrorScreen(
                getSearchResults,
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
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
            typingGetSearchResults = { _ -> },
            getSearchResults = { },
            searchUiState = SearchUiState.None,
            onTutorCardClicked = {}
        )
    }
}
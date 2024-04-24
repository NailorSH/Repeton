package com.nailorsh.repeton.features.userprofile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.navigation.NavigationRoute
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.presentation.ui.components.ProfileHeader
import com.nailorsh.repeton.features.userprofile.presentation.ui.components.ProfileOptions
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileScreenUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(
    profileScreenUiState: ProfileScreenUiState,
    sideEffectState: SharedFlow<NavigationRoute>,
    onOptionNavigate: (NavigationRoute) -> Unit,
    onOptionClicked: (Options) -> Unit
) {

    LaunchedEffect(sideEffectState) {

    }


    when (profileScreenUiState) {
        ProfileScreenUiState.Error -> Text("АШИБКА")
        ProfileScreenUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is ProfileScreenUiState.Success -> ProfileScreenContent(
            profileOptions = profileScreenUiState.profileOptions,
            settingsOptions = profileScreenUiState.settingsOptions,
            onOptionClicked = onOptionClicked,
        )
    }


}


@Composable
fun ProfileScreenContent(
    profileOptions: List<Options>,
    settingsOptions: List<Options>,
    onOptionClicked: (Options) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.top_padding)))
        ProfileHeader()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.profile_screen_profile),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )

        ProfileOptions(
            optionsList = profileOptions,
            onOptionClicked = onOptionClicked,
            isSettings = false
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.profile_screen_settings),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )
        ProfileOptions(
            optionsList = settingsOptions,
            onOptionClicked = onOptionClicked,
            isSettings = true

        )

    }
}

@Preview(
    showSystemUi = true,
)
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        profileOptions = listOf(),
        settingsOptions = listOf(),
        onOptionClicked = {}
    )
}
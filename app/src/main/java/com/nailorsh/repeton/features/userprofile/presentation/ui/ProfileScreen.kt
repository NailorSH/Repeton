package com.nailorsh.repeton.features.userprofile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.features.navigation.routes.BottomBarScreen
import com.nailorsh.repeton.features.userprofile.presentation.ui.components.ProfileHeader
import com.nailorsh.repeton.features.userprofile.presentation.ui.components.ProfileOptions
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileScreenOption
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(
    profileScreenUiState: ProfileScreenUiState,
    sideEffectState : StateFlow<BottomBarScreen>,
    onOptionNavigate : (BottomBarScreen) -> Unit
) {

    LaunchedEffect(sideEffectState) {
        onOptionNavigate(sideEffectState.value)
    }


    when (profileScreenUiState) {
        ProfileScreenUiState.Error -> Text("АШИБКА")
        ProfileScreenUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is ProfileScreenUiState.Success -> ProfileScreenContent(
            profileScreenUiState.profileOptions,
            profileScreenUiState.settingsOptions
        )
    }




}


@Composable
fun ProfileScreenContent(
    profileOptions: List<ProfileScreenOption>,
    settingsOptions: List<ProfileScreenOption>
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
            text = "Профиль",
            style = MaterialTheme.typography.labelLarge
        )

        ProfileOptions(profileOptions)
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.labelLarge
        )
        ProfileOptions(settingsOptions)

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
    )
}
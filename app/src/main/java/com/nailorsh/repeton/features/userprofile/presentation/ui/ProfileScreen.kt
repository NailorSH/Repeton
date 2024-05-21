package com.nailorsh.repeton.features.userprofile.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.navigation.NavigationRoute
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.presentation.ui.components.ProfileHeader
import com.nailorsh.repeton.features.userprofile.presentation.ui.components.ProfileOptions
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileScreenState
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.ProfileScreenUiState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ProfileScreen(
    profileScreenUiState: ProfileScreenUiState,
    sideEffectState: SharedFlow<NavigationRoute>,
    onOptionNavigate: (NavigationRoute) -> Unit,
    onOptionClicked: (Options) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectState) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sideEffectState.collect { sideEffect ->
                onOptionNavigate(sideEffect)
            }
        }
    }


    when (profileScreenUiState) {
        ProfileScreenUiState.Error -> { }
        ProfileScreenUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is ProfileScreenUiState.Success -> ProfileScreenContent(
            profileScreenState = profileScreenUiState.state,
            onOptionClicked = onOptionClicked,
        )
    }


}


@Composable
fun ProfileScreenContent(
    profileScreenState: ProfileScreenState,
    onOptionClicked: (Options) -> Unit,
) {
    val scrollState = rememberScrollState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileHeader(
                profileUserData = profileScreenState.profileUserData
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.profile_screen_profile),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            )

            ProfileOptions(
                optionsList = profileScreenState.profileOptions,
                onOptionClicked = onOptionClicked,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.profile_screen_settings),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            )
            ProfileOptions(
                optionsList = profileScreenState.settingsOptions,
                onOptionClicked = onOptionClicked,
            )

        }
    }

}

//@Preview(
//    showSystemUi = true,
//)
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreenContent(
//        profileOptions = listOf(),
//        settingsOptions = listOf(),
//        onOptionClicked = {},
//    )
//}
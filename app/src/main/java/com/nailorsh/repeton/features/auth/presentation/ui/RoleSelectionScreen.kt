package com.nailorsh.repeton.features.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R

@Composable
fun RoleSelectionScreen() {
    val selectedRole = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.registration_choose_role),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large))
        )

        Row {
            RoleButton(
                role = stringResource(R.string.pupil),
                onClick = { selectedRole.value = "student" },
                modifier = Modifier.weight(0.5f)
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))

            RoleButton(
                role = stringResource(R.string.tutor),
                onClick = { selectedRole.value = "tutor" },
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Composable
fun RoleButton(
    role: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        Text(
            text = role,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RoleSelectionScreen()
}
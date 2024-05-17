package com.nailorsh.repeton.features.auth.presentation.ui.signup

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R
import com.nailorsh.repeton.features.auth.data.model.UserData

@Composable
fun NameInputScreen(
    newUserState: UserData,
    setNameAndSurname: (String, String) -> Unit,
    onCompleted: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }
    val isTutor by remember { mutableStateOf(newUserState.canBeTutor) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_extra_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isTutor) stringResource(R.string.enter_name_surname_tutor) else
                    stringResource(R.string.enter_name_surname_pupil),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_large))
            )

            NameTextField(
                title = stringResource(R.string.name),
                name = firstName,
                onValueChanged = { firstName = it },
                isError = isFirstNameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_medium)),
            )

            NameTextField(
                title = stringResource(R.string.surname),
                name = lastName,
                onValueChanged = { lastName = it },
                isError = isLastNameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_large)),
            )

            Button(
                onClick = {
                    Log.d("FORM", "canBeTutor = $isTutor")

                    isFirstNameError = firstName.isBlank()
                    isLastNameError = lastName.isBlank()

                    if (!isFirstNameError && !isLastNameError) {
                        setNameAndSurname(firstName, lastName)
                        onCompleted()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.continues),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun NameTextField(
    title: String,
    name: String,
    onValueChanged: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChanged,
        label = {
            Text(title)
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Words
        ),
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewNameInputScreen() {
    NameInputScreen(UserData(), { _, _ -> }, {})
}
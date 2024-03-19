package com.nailorsh.repeton.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@Composable
fun EnterPhoneNumberUI(
    modifier: Modifier = Modifier
        .padding(vertical = 56.dp, horizontal = 24.dp),
    onClick: () -> Unit, phone: String,
    onPhoneChange: (String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit)?
) {
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.verify_phone),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.gif_momo_sign_up_message))

        Spacer(modifier = Modifier.height(20.dp))

        PhoneNumberTextField(
            isError = isError,
            phone = phone,
            onNumberChange = onPhoneChange,
            onDone = onDone
        )

        Button(
            onClick = {
                if (phone.isNotEmpty()) {
                    onClick()
                } else {
                    isError = true
                }

            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}


@Composable
fun PhoneNumberTextField(
    phone: String,
    onNumberChange: (String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit)?,
    isError: Boolean
) {
    OutlinedTextField(
        value = phone, onValueChange = onNumberChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = onDone),
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Default.Phone, contentDescription = "")
        },
        isError = isError,
//        prefix = { Text(text = "+7") },
//        supportingText = { if(isError) Text(text = "Неверно набранный номер") }
    )
}
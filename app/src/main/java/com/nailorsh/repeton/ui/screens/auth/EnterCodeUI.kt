package com.nailorsh.repeton.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.utils.PinVisualTransformation

@Composable
fun EnterCodeUI(
    code: String,
    onCodeChange: (String) -> Unit,
    phone: String,
    onGo: (KeyboardActionScope.() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 56.dp,
                horizontal = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = phone,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Waiting to automatically detect an SMS sent to $phone"
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = code, onValueChange = onCodeChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = onGo),
            singleLine = true,
            label = { Text(text = stringResource(R.string.code)) },
            visualTransformation = PinVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.45f)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.enter_digits_code))
    }

}
package com.nailorsh.repeton.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.OtpTextField

@Composable
fun AuthorizationScreen(
    onClick: (phoneNumber: String, otp: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PhoneNumberInput { phoneNumber ->
            onClick(phoneNumber, "")
        }

        Spacer(modifier = Modifier.height(40.dp))

        OtpInput { otpVal ->
            onClick("", otpVal)
        }
    }
}

@Composable
fun PhoneNumberInput(
    onButtonClicked: (String) -> Unit
) {
    val phoneNumber = remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = {
                Text(
                    text = stringResource(R.string.phone_number),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            placeholder = {
                Text(text = stringResource(R.string.enter_phone_number))
            },
            textStyle = MaterialTheme.typography.titleMedium,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = stringResource(R.string.phone_number)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            isError = isError,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.9f),
            onClick = {
                if (phoneNumber.value.isNotEmpty()) {
                    onButtonClicked(phoneNumber.value)
                } else {
                    isError = true
                }
            }
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = stringResource(R.string.get_otp_by_sms),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun OtpInput(
    onButtonClicked: (String) -> Unit
) {
    var otpVal: String? = null

    Text(
        text = stringResource(R.string.enter_the_otp),
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(10.dp))
    OtpTextField(
        length = 6,
    ) { getOpt ->
        otpVal = getOpt
    }
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = {
            if (otpVal != null) {
                onButtonClicked(otpVal!!)
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(45.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = stringResource(R.string.otp_verify),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun OTPScreenPreview() {
    MaterialTheme {
        AuthorizationScreen { _, _ -> }
    }
}
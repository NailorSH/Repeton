package com.nailorsh.repeton.features.subjects.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice


@Composable
fun SubjectItem(
    subject: SubjectWithPrice,
    price: String,
    onPriceSave: (SubjectWithPrice, String) -> Unit,
    onRemoveSubject: () -> Unit,
) {
    var price by rememberSaveable {
        mutableStateOf(price)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(start = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_subjects),
                    contentDescription = null
                )
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = subject.subject.name,

                    )
            }

            IconButton(
                onClick = onRemoveSubject,
                modifier = Modifier.weight(0.2f),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )
            }


        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            TextField(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxWidth(),
                value = price,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        price = newValue
                    }
                },
                supportingText = {
                    Row {
                        Text(stringResource(R.string.subject_screen_price_supporting_text))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                trailingIcon = {
                    IconButton(onClick = { onPriceSave(subject, price) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null
                        )
                    }
                },
                leadingIcon = {
                    IconButton(onClick = { price = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cancel_w400),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(stringResource(R.string.subject_screen_lesson_price)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.weight(0.1f))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SubjectItemPreview() {
    SubjectItem(
        price = "1000",
        subject = SubjectWithPrice(
            Subject(
                id = Id("0"),
                name = "Английский"
            ), 0
        ),
        onRemoveSubject = {},
        onPriceSave = { _, _ -> }
    )
}
package com.nailorsh.repeton.features.about.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.features.about.data.model.AboutContactItem

@Composable
fun AboutContacts(
    contacts: List<AboutContactItem>,
    onContactUpdate: (AboutContactItem, String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_contacts), contentDescription = null)
            Text(
                text = stringResource(R.string.my_contacts),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(
                top = 12.dp, bottom = 18.dp, start = 24.dp, end = 24.dp
            )
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            contacts.forEach { contact ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Image(
                        painter = painterResource(id = contact.icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    TextField(
                        value = contact.value,
                        onValueChange = {
                            onContactUpdate(contact, it)
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,

                            ),
                        placeholder = { Text(stringResource(contact.placeholder)) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AboutContactsPreview() {
    AboutContacts(
        listOf(
            AboutContactItem.Telegram(),
            AboutContactItem.WhatsApp()
        )
    ) { _, _ -> }
}
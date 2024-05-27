package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.contact.Contact
import com.nailorsh.repeton.common.data.models.contact.ContactType
import com.nailorsh.repeton.core.ui.theme.RepetonTheme

@Composable
fun Contacts(
    contacts: List<Contact>,
    modifier: Modifier = Modifier,
    @StringRes title: Int = R.string.my_contacts,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            items(contacts) {
                ContactItem(
                    contact = it,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}

@Preview
@Composable
fun ContactsPreview() {
    RepetonTheme {
        Contacts(
            contacts = listOf(
                Contact(
                    id = Id(""),
                    value = "ddafs",
                    type = ContactType.TELEGRAM.value
                ),
                Contact(
                    id = Id(""),
                    value = "adsadsdf",
                    type = ContactType.WHATSAPP.value
                ),
                Contact(
                    id = Id(""),
                    value = "adsadsdf",
                    type = ContactType.OTHER.value
                )
            )
        )
    }
}


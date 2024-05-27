package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.contact.Contact
import com.nailorsh.repeton.common.data.models.contact.ContactType
import com.nailorsh.repeton.core.ui.components.InfoBlockWithLabel
import com.nailorsh.repeton.core.ui.theme.RepetonTheme

@Composable
fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    InfoBlockWithLabel(
        label = contact.type,
        labelStyle = MaterialTheme.typography.labelLarge,
        modifier = modifier
    ) {
        val painter = when (ContactType.getTypeByString(contact.type)) {
            ContactType.WHATSAPP -> painterResource(R.drawable.ic_whatsapp)
            ContactType.TELEGRAM -> painterResource(R.drawable.ic_telegram)
            ContactType.OTHER -> painterResource(R.drawable.ic_info)
        }

        Icon(
            painter = painter,
            contentDescription = contact.type,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(dimensionResource(R.dimen.padding_large))
        )
    }
}

@Preview
@Composable
fun ContactItemPreview() {
    RepetonTheme {
        ContactItem(
            contact = Contact(
                id = Id(""),
                value = "ddafs",
                type = ContactType.TELEGRAM.value
            )
        )
    }
}
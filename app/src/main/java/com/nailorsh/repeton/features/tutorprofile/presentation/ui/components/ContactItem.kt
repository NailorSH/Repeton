package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.contact.Contact
import com.nailorsh.repeton.common.data.models.contact.ContactType
import com.nailorsh.repeton.core.ui.theme.RepetonTheme

@Composable
fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier,
) {

    val painter = when (ContactType.getTypeByString(contact.type)) {
        ContactType.WHATSAPP -> painterResource(R.drawable.ic_whatsapp)
        ContactType.TELEGRAM -> painterResource(R.drawable.ic_telegram)
        ContactType.OTHER -> painterResource(R.drawable.ic_info)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Image(
            painter = painter,
            contentDescription = contact.type,
            modifier = Modifier.size(dimensionResource(R.dimen.padding_large))
        )
        Text(
            text = contact.value, style = MaterialTheme.typography.bodyLarge
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
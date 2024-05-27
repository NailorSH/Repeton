package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.location.Country
import com.nailorsh.repeton.core.ui.components.UserImage

@Composable
fun ProfileInfoRow(
    profileImageSrc: String?,
    name: String,
    country: Country?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        UserImage(
            photoSrc = profileImageSrc,
            size = dimensionResource(R.dimen.tutor_profile_photo_size)
        )

        Column(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_medium))
                .weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium
            )

            country?.let {
                Row {
                    Text(
                        text = it.name["ru"] ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = it.getFlagEmoji(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.padding_extra_small))
                    )
                }
            }
        }
    }
}
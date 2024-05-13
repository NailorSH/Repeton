package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.location.Country

@Composable
fun ProfileInfoRow(
    @DrawableRes profileImageId: Int,
    name: String,
    country: Country?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = profileImageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(dimensionResource(R.dimen.tutor_profile_photo_size))
                .clip(RoundedCornerShape(5.dp))
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
                        text = it.name?.get("ru") ?: "",
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
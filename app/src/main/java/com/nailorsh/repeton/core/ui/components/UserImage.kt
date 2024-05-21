package com.nailorsh.repeton.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.SubcomposeAsyncImage
import com.nailorsh.repeton.R

@Composable
fun UserImage(photoSrc : String?, size : Dp) {
    Box {
        val shape = remember {
            mutableIntStateOf(0)
        }
        SubcomposeAsyncImage(
            onSuccess = {
                shape.intValue = 50
            },
            loading = {
                CircularProgressIndicator()
            },
            error = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile_icon),
                    contentDescription = null,
                )
            },
            contentScale = ContentScale.Crop,
            model = photoSrc,
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(percent = shape.intValue))
        )
    }
}
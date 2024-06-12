package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopTutorProfileBar(
    onBackClick: () -> Unit,
//    onShareClick: () -> Unit,
//    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
//        actions = {
//            IconButton(onClick = onShareClick) {
//                Icon(
//                    Icons.Filled.Share,
//                    contentDescription = stringResource(R.string.share)
//                )
//            }
//            IconButton(onClick = onFavoriteClick) {
//                Icon(
//                    Icons.Filled.Favorite,
//                    contentDescription = stringResource(R.string.favorite)
//                )
//            }
//        },
        modifier = modifier
    )
}
package com.nailorsh.repeton.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.theme.RepetonTheme

@Composable
fun SearchScreen() {

}

@Composable
fun TutorCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(15.dp)
    ) {
        Column() {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.man_photo),
                    contentDescription = "Репетитор",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(81.dp)
                        .height(81.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(text = "hello")
                        Icon(
                            painter = painterResource(id = R.drawable.heart_icon),
                            contentDescription = ""
                        )
                    }
//                    Row() {
//                        Row() {
//                            Icon(painter =, contentDescription =)
//                            Text(text =)
//                        }
//                        Row() {
//                            Icon(painter =, contentDescription =)
//                            Text(text =)
//                        }
//                    }
                }
            }
        }
    }
}

@Composable
fun SearchLine() {

}

@Preview
@Composable
fun TutorCardPreview() {
    RepetonTheme {
        TutorCard()
    }
}
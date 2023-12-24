package com.nailorsh.repeton.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.components.SearchBarWithFilter
import com.nailorsh.repeton.ui.theme.AmbientColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import com.nailorsh.repeton.ui.theme.SpotColor
import com.nailorsh.repeton.ui.theme.StarColor
import com.nailorsh.repeton.ui.theme.White

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(vertical = 15.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            SearchBarWithFilter(
                placeholder = R.string.search_placeholder,
                leadingIcon = R.drawable.search_icon,
                filterIcon = R.drawable.filter_icon,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                value = query,
                onValueChanged = { query = it },
                modifier = Modifier.fillMaxWidth()
            )

            TutorList()
        }
    }
}

@Composable
fun TutorList(modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item { TutorCard() }
        item { TutorCard() }
        item { TutorCard() }
    }
}

@Composable
fun TutorCard(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, spotColor = SpotColor, ambientColor = AmbientColor)
            .background(color = White, shape = RoundedCornerShape(size = 5.dp))
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.man_photo),
                    contentDescription = "Репетитор",
                    contentScale = ContentScale.Crop,
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
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Александр Витальевич",
                            style = TextStyle(
                                fontSize = 22.sp,
                                lineHeight = 26.sp,
                                fontWeight = FontWeight(800),
                                color = Color.Black,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.heart_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 9.dp, top = 5.dp)
                                .width(21.2.dp)
                                .height(20.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                                tint = StarColor,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(1.dp)
                                    .height(15.dp)
                            )
                            Text(
                                text = "4,93",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 26.sp,
                                    fontWeight = FontWeight(500),
                                    color = Color.Black,
                                )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.comment_icon),
                                tint = Color.Black,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(1.dp)
                                    .height(15.dp)
                            )
                            Text(
                                text = "100 отзывов",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 26.sp,
                                    fontWeight = FontWeight(500),
                                    color = Color.Black,
                                )
                            )
                        }
                    }


                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                var seeMore by remember { mutableStateOf(true) }

                Text(
                    text = "Я являюсь техническим руководителем проекта «Учу на Профи.Ру» и активно использую современные технологии распознавания ",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black,
                    ),
                    maxLines = if (seeMore) 3 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis,
                )

                val textButton = if (seeMore) {
                    stringResource(id = R.string.see_more)
                } else {
                    stringResource(id = R.string.see_less)
                }
                Text(
                    text = textButton,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF3D3D3D),
                    ),
                    modifier = Modifier
                        .clickable {
                            seeMore = !seeMore
                        }
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Предметы",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black,
                        )
                    )
                    Text(
                        text = "Математика • Информатика",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(400),
                            color = Color.Black,
                        )
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Образование",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black,
                        )
                    )
                    Text(
                        text = "Окончил МФТИ, ФОПФ, два красных диплома, 2005 г.",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(400),
                            color = Color.Black,
                        )
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Цены занятий",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black,
                        ),
                    )
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Математика",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Color.Black,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "500-1000 ₽ / 60 мин",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Color.Black,
                            ),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Информатика",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Color.Black,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "800-1000 ₽ / 60 мин",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Color.Black,
                            ),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF3856BF),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .padding(horizontal = 5.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Написать",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight(600),
                    color = Color.White,
                )
            )
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun TutorCardPreview() {
    RepetonTheme {
        TutorCard()
    }
}


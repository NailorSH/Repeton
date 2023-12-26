package com.nailorsh.repeton.ui.screens

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.theme.AddLessonButtonColor
import com.nailorsh.repeton.ui.theme.LineColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import com.nailorsh.repeton.ui.theme.ScreenBackground
import java.nio.file.WatchEvent

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ScreenBackground)
    )
    {
        Divider(
            modifier = Modifier
                .padding(top = 47.6.dp)
                .width(291.dp)
                .align(Alignment.CenterHorizontally),
            color = LineColor,
            thickness = 1.dp,

            )
        Row(
            modifier = Modifier
                .padding(top = 37.dp)
                .width(291.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(23.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        )
            {
                val avatarImage = painterResource(R.drawable.peepo)
                Image(
                    painter = avatarImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(81.dp)
                        .height(81.dp)
                        .padding(horizontal = 0.dp)
                )
                Column(
                ) {
                    Text(
                        text = "Иван Иванов",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                        )
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(size = 5.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(151.dp)
                            .padding(top = 16.dp)
                            .height(32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AddLessonButtonColor,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Настройки",
                            color = Color.White,
                            lineHeight = 32.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        Column(
            modifier = Modifier
                .width(299.dp)
                .align(Alignment.CenterHorizontally)
        )
        {
            AboutMeBox("О себе", "Учусь в 11 классе. Планирую поступать в технический вуз, готовлюсь сдавать ЕГЭ по физике и математике")
        }
    }
}
@Composable
fun AboutMeBox(title: String, information: String) {
    Box(
        modifier = Modifier
            .padding(top = 53.dp)
            .width(299.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 5.dp))
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
        )
        {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
            Text(
                text = information,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 40.dp),
            )
        }
    }
}

@Preview(
    showSystemUi = true,
)
@Composable
fun ProfileScreenPreview() {
    RepetonTheme {
        ProfileScreen()
    }
}
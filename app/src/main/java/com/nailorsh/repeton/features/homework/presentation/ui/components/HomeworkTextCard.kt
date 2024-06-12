package com.nailorsh.repeton.features.homework.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.core.ui.components.UserImage
import com.nailorsh.repeton.features.homework.data.models.HomeworkUser

@Composable
fun HomeworkTextCard(
    author : HomeworkUser,
    homeworkText : String,
) {
    OutlinedCard(
        shape = RectangleShape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
        // border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UserImage(photoSrc = author.photoSrc, size = 48.dp)
                Box(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    Text("${author.name} ${author.surname}", style = MaterialTheme.typography.titleMedium)
                }
                Row(modifier = Modifier.alpha(0.5f), verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_history),
//                        contentDescription = null
//                    )
//                    Text("9ч назад", style = MaterialTheme.typography.labelSmall)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            Text(homeworkText, style = MaterialTheme.typography.bodyLarge)
        }


    }
}

//
//@Preview
//@Composable
//fun HomeworkTextCardPreview() {
//    HomeworkTextCard()
//}
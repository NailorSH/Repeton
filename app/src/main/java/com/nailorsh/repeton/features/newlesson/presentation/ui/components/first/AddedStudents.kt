package com.nailorsh.repeton.features.newlesson.presentation.ui.components.first

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.UserImage
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem

@Composable
fun AddedStudents(
    students: List<NewLessonUserItem>,
    onRemoveUser: (NewLessonUserItem) -> Unit,
    onAddUserShowDialogue: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
        ) {
            ListItem(
                headlineContent = {
                    Column() {
                        Text(text = "Добавить ученика", style = MaterialTheme.typography.titleLarge)
                    }
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_add),
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable {
                    onAddUserShowDialogue()
                }
            )
        }
        students.forEach { user ->
            OutlinedCard(
                shape = RoundedCornerShape(16.dp)
            ) {
                ListItem(
                    headlineContent = {
                        Column() {
                            Text(text = user.name)
                            Text(text = user.surname)
                        }
                    },
                    leadingContent = {
                        UserImage(photoSrc = user.photoSrc, 48.dp)
                    },
                    trailingContent = {
                        IconButton(onClick = { onRemoveUser(user) }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    },
                )
            }
        }

    }
}
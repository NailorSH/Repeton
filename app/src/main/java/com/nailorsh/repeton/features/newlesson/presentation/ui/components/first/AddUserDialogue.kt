package com.nailorsh.repeton.features.newlesson.presentation.ui.components.first

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.UserImage
import com.nailorsh.repeton.features.newlesson.data.models.NewLessonUserItem

@Composable
fun AddUserDialogue(
    students: List<NewLessonUserItem>,
    onAddUser: (NewLessonUserItem) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard {
            Text(
                stringResource(R.string.new_lesson_screen_students_list_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            LazyColumn(modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)) {
                itemsIndexed(students) { index, user ->

                    ListItem(
                        leadingContent = {
                            UserImage(photoSrc = user.photoSrc, 48.dp)
                        },
                        headlineContent = {
                            Column() {
                                Text(text = user.name)
                                Text(text = user.surname)
                            }
                        },
                        trailingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person_add),
                                contentDescription = null,
                            )
                        },
                        modifier = Modifier.clickable { onAddUser(user) }
                    )

                    if (index != students.size - 1) {
                        HorizontalDivider()
                    }

                }

            }
        }

    }
}
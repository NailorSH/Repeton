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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.user.User

@Composable
fun AddUserDialogue(
    students: List<User>,
    onAddUser: (User) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard {
            LazyColumn(modifier = Modifier.padding(vertical = 32.dp)) {
                itemsIndexed(students) { index, user ->

                    ListItem(
                        leadingContent = {
                            UserListImage(photoSrc = user.photoSrc)
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
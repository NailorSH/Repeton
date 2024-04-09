package com.nailorsh.repeton.features.messenger.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.core.ui.components.PlugScreen
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.core.ui.theme.White
import com.nailorsh.repeton.features.messenger.data.model.Chat
import com.nailorsh.repeton.features.messenger.presentation.viewmodel.ChatsUiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    getChats: () -> Unit,
    chatsUiState: ChatsUiState,
) {
    getChats()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Сообщения") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = White
                )
            )
        },
        content = {
            PlugScreen(modifier = Modifier.padding(it))
//            when (chatsUiState) {
//                is ChatsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
//
//                is ChatsUiState.Success -> ChatList(
//                    modifier = Modifier.padding(it),
//                    chats = chatsUiState.chats
//                )
//
//                is ChatsUiState.Error -> ErrorScreen(
//                    getSearchResults,
//                    modifier = modifier.fillMaxSize()
//                )
//
//                else -> {}
//            }

        }
    )
}

@Composable
fun ChatList(
    chats: List<Chat>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(chats.size) { index ->
            ChatItem(chat = chats[index])
            if (index < chats.size - 1) {
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { TODO() }
            .padding(horizontal = 16.dp)
    ) {
        Text(text = chat.username, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(
    showSystemUi = true,
)
@Composable
fun ChatsScreenPreview() {
    RepetonTheme {
//        ChatsScreen()
    }
}
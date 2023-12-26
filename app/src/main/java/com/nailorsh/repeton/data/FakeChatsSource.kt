package com.nailorsh.repeton.data

import com.nailorsh.repeton.model.Chat

object FakeChatsSource {
    fun generateChatList(): List<Chat> {
        return List(10) { index ->
            Chat(
                id = index,
                username = "Chat $index",
                lastMessage = "Last message $index"
            )
        }
    }
}
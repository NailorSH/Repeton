package com.nailorsh.repeton.messenger.data.source

import com.nailorsh.repeton.messenger.data.model.Chat

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
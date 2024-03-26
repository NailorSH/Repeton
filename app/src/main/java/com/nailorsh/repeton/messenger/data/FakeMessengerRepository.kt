package com.nailorsh.repeton.messenger.data

import com.nailorsh.repeton.messenger.data.model.Chat
import com.nailorsh.repeton.messenger.data.source.FakeChatsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeMessengerRepository @Inject constructor() : MessengerRepository {
    override suspend fun getChats(): List<Chat> = withContext(Dispatchers.IO) {
        FakeChatsSource.generateChatList()
    }
}
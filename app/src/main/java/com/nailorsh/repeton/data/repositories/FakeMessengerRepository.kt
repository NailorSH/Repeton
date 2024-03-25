package com.nailorsh.repeton.data.repositories

import com.nailorsh.repeton.data.sources.FakeChatsSource
import com.nailorsh.repeton.domain.repositories.MessengerRepository
import com.nailorsh.repeton.model.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeMessengerRepository @Inject constructor() : MessengerRepository {
    override suspend fun getChats(): List<Chat> = withContext(Dispatchers.IO) {
        FakeChatsSource.generateChatList()
    }
}
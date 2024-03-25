package com.nailorsh.repeton.messenger.data

import com.nailorsh.repeton.messenger.data.model.Chat

interface MessengerRepository {
    suspend fun getChats(): List<Chat>
}
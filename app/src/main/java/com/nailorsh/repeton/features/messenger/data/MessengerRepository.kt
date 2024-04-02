package com.nailorsh.repeton.features.messenger.data

import com.nailorsh.repeton.features.messenger.data.model.Chat

interface MessengerRepository {
    suspend fun getChats(): List<Chat>
}
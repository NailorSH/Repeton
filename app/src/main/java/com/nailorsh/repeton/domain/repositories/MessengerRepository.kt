package com.nailorsh.repeton.domain.repositories

import com.nailorsh.repeton.model.Chat

interface MessengerRepository {
    suspend fun getChats(): List<Chat>
}
package com.nailorsh.repeton.features.messenger.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nailorsh.repeton.features.messenger.data.MessengerRepository
import com.nailorsh.repeton.features.messenger.data.model.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

sealed interface ChatsUiState {
    data class Success(val chats: List<Chat>) : ChatsUiState
    object Error : ChatsUiState
    object Loading : ChatsUiState
}

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val messengerRepository: MessengerRepository
) : ViewModel() {
    var chatsUiState: ChatsUiState by mutableStateOf(ChatsUiState.Loading)
        private set

    fun getChats() {
        viewModelScope.launch {
            chatsUiState = ChatsUiState.Loading
            delay(2000)
            chatsUiState = try {
                ChatsUiState.Success(messengerRepository.getChats())
            } catch (e: IOException) {
                ChatsUiState.Error
            } catch (e: HttpRetryException) {
                ChatsUiState.Error
            }
        }
    }
}

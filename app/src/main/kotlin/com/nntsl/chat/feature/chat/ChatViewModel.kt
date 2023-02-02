package com.nntsl.chat.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntsl.chat.core.domain.GetMessagesUseCase
import com.nntsl.chat.core.domain.SendMessageUseCase
import com.nntsl.chat.core.model.Message
import com.nntsl.chat.feature.chat.model.MessageScreenItem
import com.nntsl.chat.feature.chat.model.mapToMessageScreenItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    val chatUiState: StateFlow<ChatUiState> =
        getMessagesUseCase()
            .map {
                ChatUiState.Success(isLoading = false, messages = it.mapToMessageScreenItems())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChatUiState.NoData(isLoading = true, messages = listOf())
            )

    fun addMessage(message: Message) {
        viewModelScope.launch {
            sendMessageUseCase.invoke(message)
        }
    }
}

sealed interface ChatUiState {
    val isLoading: Boolean
    val messages: List<MessageScreenItem>

    data class Success(override val isLoading: Boolean, override val messages: List<MessageScreenItem>) : ChatUiState

    data class NoData(override val isLoading: Boolean, override val messages: List<MessageScreenItem>) : ChatUiState
}

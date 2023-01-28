package com.nntsl.chat.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntsl.chat.core.domain.GetMessagesUseCase
import com.nntsl.chat.core.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase
) : ViewModel() {

    private val viewModelState = MutableStateFlow(ChatViewModelState(isLoading = true))

    val chatUiState: StateFlow<ChatUiState> =
        viewModelState
            .map(ChatViewModelState::toUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = viewModelState.value.toUiState()
            )

    init {
        refresh()
    }

    private fun refresh() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getMessagesUseCase()
        }
    }
}

sealed interface ChatUiState {
    val isLoading: Boolean

    data class Success(override val isLoading: Boolean, val messages: List<Message>) : ChatUiState

    data class NoData(override val isLoading: Boolean) : ChatUiState
}

private data class ChatViewModelState(
    val isLoading: Boolean = false,
    val messages: List<Message> = listOf()
) {
    fun toUiState(): ChatUiState {
        return if (messages.isEmpty()) {
            ChatUiState.NoData(isLoading = false)
        } else {
            ChatUiState.Success(
                isLoading = isLoading,
                messages = messages
            )
        }
    }
}

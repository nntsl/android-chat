package com.nntsl.chat.core.testing

import com.nntsl.chat.core.data.MessagesRepository
import com.nntsl.chat.core.model.Message
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

class TestMessagesRepository : MessagesRepository {

    private val messagesFlow =
        MutableSharedFlow<List<Message>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val currentMessages: List<Message>
        get() = messagesFlow.replayCache.firstOrNull() ?: listOf()

    override val messages: Flow<List<Message>>
        get() = messagesFlow.filterNotNull()

    override suspend fun insertMessage(message: Message) {
        messagesFlow.tryEmit(currentMessages.plus(message))
    }

    /**
     * A test-only API to allow controlling the list of messages from tests.
     */
    fun sendMessages(messages: List<Message>) {
        messagesFlow.tryEmit(messages)
    }
}
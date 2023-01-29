package com.nntsl.chat.core.data

import com.nntsl.chat.core.model.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    val messages: Flow<List<Message>>

    suspend fun insertMessage(message: Message)
}

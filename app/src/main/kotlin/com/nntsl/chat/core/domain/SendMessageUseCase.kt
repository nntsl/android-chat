package com.nntsl.chat.core.domain

import com.nntsl.chat.core.data.MessagesRepository
import com.nntsl.chat.core.model.Message
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend fun invoke(message: Message) {
        return messagesRepository.insertMessage(message)
    }
}
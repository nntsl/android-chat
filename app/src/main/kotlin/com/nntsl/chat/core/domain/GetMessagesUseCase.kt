package com.nntsl.chat.core.domain

import com.nntsl.chat.core.datainfrustructure.MessagesRepository
import com.nntsl.chat.core.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke(): Flow<List<Message>> {
        return messagesRepository.messages
    }
}

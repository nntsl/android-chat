package com.nntsl.chat.core.data

import com.nntsl.chat.core.database.dao.MessagesDao
import com.nntsl.chat.core.database.model.MessageEntity
import com.nntsl.chat.core.database.model.asEntity
import com.nntsl.chat.core.database.model.asExternalModel
import com.nntsl.chat.core.model.Message
import com.nntsl.chat.core.util.currentTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstMessagesRepository @Inject constructor(
    private val messagesDao: MessagesDao
) : MessagesRepository {

    private val predefinedMessages = listOf(
        MessageEntity(
            content = "message 15",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-29T10:10:22.000Z")
        ),
        MessageEntity(
            content = "message 14",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-29T10:00:00.000Z")
        ),
        MessageEntity(
            content = "message 13",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-27T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 12",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-27T10:00:00.000Z")
        ),
        MessageEntity(
            content = "message 11",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-27T11:10:00.000Z")
        ),
        MessageEntity(
            content = "message 10",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-25T12:10:00.000Z")
        ),
        MessageEntity(
            content = "message 9",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-25T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 8",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-25T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 7",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-25T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 6",
            isUserMessage = false,
            messageDate = Instant.parse("2023-01-25T11:10:00.000Z")
        ),
        MessageEntity(
            content = "message 5",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-25T12:10:00.000Z")
        ),
        MessageEntity(
            content = "message 4",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-25T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 3",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-20T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 2",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-20T10:10:00.000Z")
        ),
        MessageEntity(
            content = "message 1",
            isUserMessage = true,
            messageDate = Instant.parse("2023-01-20T10:10:00.000Z")
        ),
    )

    override val messages: Flow<List<Message>> =
        messagesDao.getMessages()
            .map {
                if (it.isEmpty()) {
                    messagesDao.insertOrIgnoreMessages(
                        predefinedMessages
                    )
                }
                it.map(MessageEntity::asExternalModel)
            }

    override suspend fun insertMessage(message: Message) {
        messagesDao.insertMessage(message.asEntity())

        // Insert answer if message contains "test"
        if (message.content.contains("test", ignoreCase = true)) {
            messagesDao.insertMessage(
                MessageEntity(
                    content = "Answer to \"${message.content}\"",
                    isUserMessage = false,
                    messageDate = currentTime()
                )
            )
        }
    }
}

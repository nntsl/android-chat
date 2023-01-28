package com.nntsl.chat.core.data

import com.nntsl.chat.core.database.dao.MessagesDao
import com.nntsl.chat.core.database.model.MessageEntity
import com.nntsl.chat.core.database.model.asExternalModel
import com.nntsl.chat.core.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstMessagesRepository @Inject constructor(
    messagesDao: MessagesDao
) : MessagesRepository {

    override val messages: Flow<List<Message>> =
        messagesDao.getMessages().map { it.map(MessageEntity::asExternalModel) }
}

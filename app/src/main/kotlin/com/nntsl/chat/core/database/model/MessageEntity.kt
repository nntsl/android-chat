package com.nntsl.chat.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nntsl.chat.core.model.Message
import kotlinx.datetime.Instant

@Entity(tableName = "messages")
data class MessageEntity(
    val content: String,
    @ColumnInfo(name = "message_date")
    val messageDate: Instant,
    val isUserMessage: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun MessageEntity.asExternalModel() = Message(
    content = content,
    date = messageDate,
    isUserMessage = isUserMessage
)

fun Message.asEntity() = MessageEntity(
    content = content,
    messageDate = date,
    isUserMessage = isUserMessage
)

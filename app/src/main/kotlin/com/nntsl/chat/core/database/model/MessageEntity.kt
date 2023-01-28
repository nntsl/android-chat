package com.nntsl.chat.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nntsl.chat.core.model.Message
import kotlinx.datetime.Instant

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val content: String,
    @ColumnInfo(name = "message_date")
    val messageDate: Instant,
    val isUserMessage: Boolean
)

fun MessageEntity.asExternalModel() = Message(
    id = id,
    content = content,
    date = messageDate,
    isUserMessage = isUserMessage
)

package com.nntsl.chat.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nntsl.chat.core.database.dao.MessagesDao
import com.nntsl.chat.core.database.model.MessageEntity
import com.nntsl.chat.core.database.util.InstantConverter

@Database(
    entities = [
        MessageEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InstantConverter::class
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
}

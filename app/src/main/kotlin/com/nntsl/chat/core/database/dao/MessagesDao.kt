package com.nntsl.chat.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nntsl.chat.core.database.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Query(value = "SELECT * FROM messages ORDER BY message_date DESC")
    fun getMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMessages(entities: List<MessageEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(entity: MessageEntity)
}

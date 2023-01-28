package com.nntsl.chat.core.database.di

import com.nntsl.chat.core.database.ChatDatabase
import com.nntsl.chat.core.database.dao.MessagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesMessagesDao(
        database: ChatDatabase,
    ): MessagesDao = database.messagesDao()
}

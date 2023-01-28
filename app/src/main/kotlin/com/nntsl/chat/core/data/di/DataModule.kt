package com.nntsl.chat.core.data.di

import com.nntsl.chat.core.data.MessagesRepository
import com.nntsl.chat.core.data.OfflineFirstMessagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsMessagesRepository(
        messagesRepository: OfflineFirstMessagesRepository
    ): MessagesRepository
}

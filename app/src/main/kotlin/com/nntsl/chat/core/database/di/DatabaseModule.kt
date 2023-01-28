package com.nntsl.chat.core.database.di

import android.content.Context
import androidx.room.Room
import com.nntsl.chat.core.database.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSimpleChatDatabase(
        @ApplicationContext context: Context,
    ): ChatDatabase = Room.databaseBuilder(
        context,
        ChatDatabase::class.java,
        "chat-database"
    ).build()
}

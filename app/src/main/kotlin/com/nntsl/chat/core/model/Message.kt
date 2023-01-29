package com.nntsl.chat.core.model

import kotlinx.datetime.Instant

data class Message(
    val content: String,
    val date: Instant,
    val isUserMessage: Boolean
)

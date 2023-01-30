package com.nntsl.chat

import com.nntsl.chat.core.model.Message
import kotlinx.datetime.Instant

val testMessageList = listOf(
    Message(
        content = "Test message 7",
        date = Instant.parse("2023-01-30T10:10:00.000Z"),
        isUserMessage = true
    ),
    Message(
        content = "Test message 6",
        date = Instant.parse("2023-01-30T10:05:10.000Z"),
        isUserMessage = true
    ),
    Message(
        content = "Test message 5",
        date = Instant.parse("2023-01-30T10:05:00.000Z"),
        isUserMessage = true
    ),
    Message(
        content = "Test message 4",
        date = Instant.parse("2023-01-29T09:40:00.000Z"),
        isUserMessage = false
    ),
    Message(
        content = "Test message 3",
        date = Instant.parse("2023-01-29T09:39:50.000Z"),
        isUserMessage = false
    ),
    Message(
        content = "Test message 2",
        date = Instant.parse("2023-01-29T09:30:00.000Z"),
        isUserMessage = false
    ),
    Message(
        content = "Test message 1",
        date = Instant.parse("2023-01-29T09:20:00.000Z"),
        isUserMessage = true
    )
)
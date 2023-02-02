package com.nntsl.chat.feature.chat.model

import androidx.annotation.VisibleForTesting
import com.nntsl.chat.core.model.Message
import com.nntsl.chat.core.util.isDifferenceInSecMore
import com.nntsl.chat.core.util.isTheSameDay
import com.nntsl.chat.core.util.isTheSameWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class MessageScreenItem(
    val content: String,
    val dateFormatted: String,
    val isUserMessage: Boolean,
    val showSectionTitle: Boolean,
    val showMessageTail: Boolean
)

fun List<Message>.mapToMessageScreenItems(): List<MessageScreenItem> {
    val zoneId = ZoneId.systemDefault()
    return mapIndexed { index, message ->
        MessageScreenItem(
            content = message.content,
            dateFormatted = dateFormatted(publishDate = message.date, zoneId = zoneId),
            isUserMessage = message.isUserMessage,
            showSectionTitle = shouldShowSectionTitle(this, index),
            showMessageTail = shouldShowMessageTail(this, index)
        )
    }
}

// Checks if need to show tail when any of the 3 criteria are met
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun shouldShowMessageTail(messages: List<Message>, index: Int): Boolean {
    val showTail = if (index == 0) {
        true
    } else {
        isDifferenceInSecMore(messages[index - 1].date, messages[index].date, 20)
                || (messages[index - 1].isUserMessage && !messages[index].isUserMessage)
                || (!messages[index - 1].isUserMessage && messages[index].isUserMessage)
    }
    return showTail
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun shouldShowSectionTitle(messages: List<Message>, index: Int): Boolean {
    return if (index < messages.size - 1) {
        !isTheSameDay(messages[index + 1].date, messages[index].date)
    } else {
        true
    }
}

// Returns message date with format “{day} {timestamp}” (Thursday 11:59).
// If date is more than a week ago, format would be ”MMM d h:mm” (Jan 30 10:00)
private fun dateFormatted(publishDate: Instant, zoneId: ZoneId): String {
    val pattern = if (isTheSameWeek(publishDate)) "EEEE h:mm" else "MMM d h:mm"

    return DateTimeFormatter.ofPattern(pattern).withZone(zoneId).format(publishDate.toJavaInstant())
}

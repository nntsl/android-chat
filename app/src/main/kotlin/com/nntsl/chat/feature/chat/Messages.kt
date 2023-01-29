package com.nntsl.chat.feature.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.nntsl.chat.core.model.Message
import com.nntsl.chat.core.util.isDifferenceInSecMore
import com.nntsl.chat.core.util.isTheSameDay
import com.nntsl.chat.core.util.isTheSameWeek
import com.nntsl.chat.designsystem.util.MESSAGE_PADDING
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun Messages(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    resetScroll: () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            contentPadding = WindowInsets.statusBars.add(WindowInsets(top = 20.dp)).asPaddingValues(),
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val content = messages[index]

                val sameDay = if (index < messages.size - 1) {
                    isTheSameDay(messages[index + 1].date, messages[index].date)
                } else {
                    index == 0
                }
                item {
                    Message(
                        message = content,
                        isUserMessage = content.isUserMessage,
                        showTail = shouldShowMessageTail(messages, index)
                    )
                }
                if (!sameDay) {
                    item {
                        Timestamp(content)
                    }
                }
            }
        }
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 || scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                resetScroll()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

// Show message date
@Composable
private fun Timestamp(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = dateFormatted(message.date),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Message(
    message: Message,
    isUserMessage: Boolean,
    showTail: Boolean
) {
    Row(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)) {
        ChatItemBubble(message = message, isUserMessage = isUserMessage, showTail = showTail)
    }
}

@Composable
private fun ChatItemBubble(
    message: Message,
    isUserMessage: Boolean,
    showTail: Boolean
) {
    val backgroundBubbleColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val shape = getBubbleShape(message.isUserMessage, showTail)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isUserMessage) MESSAGE_PADDING else 0.dp,
                end = if (isUserMessage) 0.dp else MESSAGE_PADDING
            )
    ) {
        Surface(
            color = backgroundBubbleColor,
            shape = shape,
            modifier = Modifier
                .align(if (isUserMessage) Alignment.End else Alignment.Start)
                .defaultMinSize(minWidth = 80.dp)
        ) {
            Box() {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (isUserMessage) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                )
                if (isUserMessage) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .height(12.dp)
                            .offset(y = (-2).dp, x = (-8).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(12.dp)
                                .offset(x = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getBubbleShape(isUserMessage: Boolean, showTail: Boolean): Shape {
    val roundedCorners = RoundedCornerShape(16.dp)
    return if (showTail) {
        if (isUserMessage) {
            roundedCorners.copy(bottomEnd = CornerSize(0))
        } else {
            roundedCorners.copy(bottomStart = CornerSize(0))
        }
    } else {
        roundedCorners
    }
}

// Returns message date with format “{day} {timestamp}” (Thursday 11:59).
@Composable
private fun dateFormatted(publishDate: Instant): String {
    val pattern = if (isTheSameWeek(publishDate)) "EEEE h:mm" else "MMM d h:mm"
    val zoneId by remember { mutableStateOf(ZoneId.systemDefault()) }

    return DateTimeFormatter.ofPattern(pattern).withZone(zoneId).format(publishDate.toJavaInstant())
}

// Shows tail when any of the 3 criteria are met
private fun shouldShowMessageTail(messages: List<Message>, index: Int): Boolean {
    val showTail = if (index == 0) {
        true
    } else {
        isDifferenceInSecMore(messages[index - 1].date, messages[index].date, 20)
                || (messages[index - 1].isUserMessage && !messages[index].isUserMessage)
                || (!messages[index - 1].isUserMessage && messages[index].isUserMessage)
    }
    return showTail
}

private val JumpToBottomThreshold = 56.dp

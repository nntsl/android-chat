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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.nntsl.chat.designsystem.util.MESSAGE_PADDING
import com.nntsl.chat.feature.chat.model.MessageScreenItem

@Composable
fun MessagesContent(
    messages: List<MessageScreenItem>,
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
            modifier = Modifier.fillMaxSize()
        ) {
            messages.forEach { message ->
                item {
                    Message(
                        message = message.content,
                        isUserMessage = message.isUserMessage,
                        showTail = message.showMessageTail
                    )
                }
                if (message.showSectionTitle) {
                    item {
                        Timestamp(message.dateFormatted)
                    }
                }
            }
        }
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0
                        || scrollState.firstVisibleItemScrollOffset > jumpThreshold
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
private fun Timestamp(date: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Message(
    message: String,
    isUserMessage: Boolean,
    showTail: Boolean
) {
    Row(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)) {
        ChatItemBubble(message = message, isUserMessage = isUserMessage, showTail = showTail)
    }
}

@Composable
private fun ChatItemBubble(
    message: String,
    isUserMessage: Boolean,
    showTail: Boolean
) {
    val backgroundBubbleColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val shape = getBubbleShape(isUserMessage, showTail)

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
                    text = message,
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

private val JumpToBottomThreshold = 56.dp

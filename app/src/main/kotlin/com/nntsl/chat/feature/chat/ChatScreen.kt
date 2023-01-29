package com.nntsl.chat.feature.chat

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntsl.chat.R.string
import com.nntsl.chat.core.model.Message
import com.nntsl.chat.core.util.currentTime
import com.nntsl.chat.designsystem.component.ChatLoadingWheel
import kotlinx.coroutines.launch

@Composable
fun ChatRoute(onBackClick: () -> Unit, viewModel: ChatViewModel = hiltViewModel()) {

    val uiState by viewModel.chatUiState.collectAsStateWithLifecycle()

    ChatScreen(
        onBackClick = onBackClick,
        uiState = uiState,
        modifier = Modifier,
        addMessage = viewModel::addMessage,
        otherUserName = "Sarah"
    )
}

@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    uiState: ChatUiState,
    addMessage: (Message) -> Unit,
    modifier: Modifier,
    otherUserName: String
) {
    val loading = uiState.isLoading

    if (!loading) {
        val localView = LocalView.current
        LaunchedEffect(Unit) {
            val activity = localView.context as? Activity ?: return@LaunchedEffect
            localView.doOnPreDraw { activity.reportFullyDrawn() }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ChatContent(
            onBackClick = onBackClick,
            modifier = modifier,
            uiState = uiState,
            addMessage = addMessage,
            otherUserName = otherUserName
        )
    }

    // Show custom progress when state is Loading
    AnimatedVisibility(
        visible = loading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        val loadingContentDescription = stringResource(id = string.messages_loading)
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ChatLoadingWheel(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 60.dp),
                contentDesc = loadingContentDescription
            )
        }
    }
}

@Composable
fun ChatHeader(
    onBackClick: () -> Unit,
    name: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 12.dp)
    ) {
        IconButton(
            modifier = Modifier,
            onClick = { onBackClick() },
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "chat:back",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Image(
            imageVector = Icons.Rounded.Person,
            contentDescription = "chat:avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        IconButton(
            modifier = Modifier,
            onClick = { onBackClick() },
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "chat:menu",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.rotate(180f)
            )
        }
    }
}

@Composable
private fun ChatContent(
    uiState: ChatUiState,
    otherUserName: String,
    modifier: Modifier = Modifier,
    addMessage: (Message) -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(onBackClick = onBackClick, name = otherUserName)
        Box(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )
        if (uiState.messages.isEmpty() && !uiState.isLoading) {
            Text(
                text = stringResource(string.empty_header),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Messages(
            messages = uiState.messages,
            modifier = Modifier.weight(1f),
            scrollState = scrollState,
            resetScroll = {
                scope.launch {
                    scrollState.scrollToItem(0)
                }
            }
        )
        Box(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        )
        UserInput(
            onMessageSent = { content ->
                addMessage(
                    Message(
                        content = content,
                        isUserMessage = true,
                        date = currentTime()
                    )
                )
            },
            resetScroll = {
                scope.launch {
                    scrollState.scrollToItem(0)
                }
            },
            modifier = Modifier
//                .windowInsetsPadding(
//                    WindowInsets.safeDrawing.only(
//                        WindowInsetsSides.Bottom
//                    )
//                )
//                .navigationBarsPadding()
//                .imePadding()
        )
    }
}

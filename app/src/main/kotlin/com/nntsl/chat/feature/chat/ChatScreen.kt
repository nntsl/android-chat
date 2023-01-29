package com.nntsl.chat.feature.chat

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.MoreVert
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
        otherUserName = "Sarah",
        otherUserPhoto = "https://www.shutterstock.com/image-photo/happy-young-woman-sitting-on-260nw-2018571389.jpg"
    )
}

@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    uiState: ChatUiState,
    addMessage: (Message) -> Unit,
    modifier: Modifier,
    otherUserName: String,
    otherUserPhoto: String
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
            uiState = uiState,
            otherUserName = otherUserName,
            otherUserPhoto = otherUserPhoto,
            addMessage = addMessage,
            onBackClick = onBackClick
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
    name: String,
    photo: String
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
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "chat:back",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo)
                .allowHardware(false)
                .crossfade(true)
                .build(),
            contentDescription = "chat:avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
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
                modifier = Modifier.rotate(90f)
            )
        }
    }
}

@Composable
private fun ChatContent(
    uiState: ChatUiState,
    otherUserName: String,
    otherUserPhoto: String,
    addMessage: (Message) -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        ChatHeader(
            onBackClick = onBackClick,
            name = otherUserName,
            photo = otherUserPhoto
        )
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
            }
        )
    }
}

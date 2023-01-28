package com.nntsl.chat.feature.chat

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.nntsl.chat.designsystem.component.ChatLoadingWheel

@Composable
fun ChatRoute(onBackClick: () -> Unit, viewModel: ChatViewModel = hiltViewModel()) {

    val uiState by viewModel.chatUiState.collectAsStateWithLifecycle()

    ChatScreen(onBackClick = onBackClick, uiState = uiState, modifier = Modifier)
}

@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    uiState: ChatUiState,
    modifier: Modifier
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
        when (uiState) {
            is ChatUiState.NoData -> {
                ChatEmptyScreen(onBackClick = onBackClick, modifier = modifier)
            }
            is ChatUiState.Success -> {
                ChatContent(onBackClick = onBackClick, modifier = modifier, messages = uiState.messages)
            }
        }
    }
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
                modifier = Modifier.align(Alignment.Center),
                contentDesc = loadingContentDescription
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatEmptyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        stickyHeader {
            ChatHeader(onBackClick = onBackClick, name = "Name Surname")
        }
        item {
            Text(
                text = stringResource(string.empty_header),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    messages: List<Message>
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .statusBarsPadding()
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize()
    ) {
        stickyHeader {
            ChatHeader(onBackClick = onBackClick, name = "Name Surname")
        }
        chatMessages(
            messages = messages,
            modifier = Modifier,
            scrollState = scrollState
        )
    }
}

@Composable
private fun ChatHeader(
    onBackClick: () -> Unit,
    name: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
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
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

private fun LazyListScope.chatMessages(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    scrollState: LazyListState
) {

}

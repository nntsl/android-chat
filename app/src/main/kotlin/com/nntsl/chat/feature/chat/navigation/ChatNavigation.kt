package com.nntsl.chat.feature.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nntsl.chat.feature.chat.ChatRoute

const val chatRoute = "chat_route"

fun NavGraphBuilder.chatScreen(onBackClick: () -> Unit) {
    composable(route = chatRoute) {
        ChatRoute(onBackClick = onBackClick)
    }
}

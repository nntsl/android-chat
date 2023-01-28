package com.nntsl.chat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nntsl.chat.feature.chat.navigation.chatRoute
import com.nntsl.chat.feature.chat.navigation.chatScreen

@Composable
fun ChatNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = chatRoute
    ) {
        chatScreen(onBackClick = { navController.popBackStack() })
    }
}
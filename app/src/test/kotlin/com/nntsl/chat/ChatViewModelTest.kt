package com.nntsl.chat

import com.nntsl.chat.core.data.TestMessagesRepository
import com.nntsl.chat.core.domain.GetMessagesUseCase
import com.nntsl.chat.core.domain.SendMessageUseCase
import com.nntsl.chat.core.model.Message
import com.nntsl.chat.core.util.MainDispatcherRule
import com.nntsl.chat.feature.chat.ChatUiState
import com.nntsl.chat.feature.chat.ChatViewModel
import com.nntsl.chat.feature.chat.model.mapToMessageScreenItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val messagesRepository = TestMessagesRepository()

    private val getMessagesUseCase = GetMessagesUseCase(
        messagesRepository = messagesRepository
    )

    private val sendMessageUseCase = SendMessageUseCase(
        messagesRepository = messagesRepository
    )

    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        viewModel = ChatViewModel(
            getMessagesUseCase = getMessagesUseCase,
            sendMessageUseCase = sendMessageUseCase
        )
    }

    @Test
    fun uiState_whenInitialized_thenShowLoading() = runTest {
        assertEquals(ChatUiState.NoData(isLoading = true, messages = listOf()), viewModel.chatUiState.value)
    }

    @Test
    fun uiState_whenMessagesLoaded_thenShowSuccess() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.chatUiState.collect() }

        messagesRepository.sendMessages(testMessageList)

        assertEquals(
            ChatUiState.Success(
                isLoading = false,
                messages = testMessageList.mapToMessageScreenItems()
            ),
            viewModel.chatUiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun uiState_whenSendMessage_thenShowUpdatedMessageList() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.chatUiState.collect() }

        val message = Message(
            content = "Test message 8",
            Instant.parse("2023-01-30T09:30:00.000Z"),
            isUserMessage = true
        )

        messagesRepository.sendMessages(testMessageList)
        messagesRepository.insertMessage(message)

        assertEquals(
            ChatUiState.Success(
                isLoading = false,
                messages = testMessageList.plus(message).mapToMessageScreenItems()
            ),
            viewModel.chatUiState.value
        )

        collectJob.cancel()
    }
}

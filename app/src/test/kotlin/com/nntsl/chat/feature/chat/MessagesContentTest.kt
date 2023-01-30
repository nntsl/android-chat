package com.nntsl.chat.feature.chat

import com.nntsl.chat.testMessageList
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(JUnitParamsRunner::class)
class MessagesContentTest {

    @Test
    @Parameters(
        value = [
            "6, true",  // my, 09:20:00
            "5, true",  // other, 09:30:00
            "4, false", // other, 09:39:50
            "3, true",  // other, 09:40:00
            "2, false", // my, 10:05:00
            "1, true",  // my, 10:05:10
            "0, true"   // my, 10:10:00
        ]
    )
    fun shouldShowMessageTailTest(index: Int, expected: Boolean) {
        val actual = shouldShowMessageTail(testMessageList, index)

        assertEquals(actual, expected)
    }

    @Test
    @Parameters(
        value = [
            "6, true",  // 2023-01-29
            "5, false", // 2023-01-29
            "4, false", // 2023-01-29
            "3, false", // 2023-01-29
            "2, true",  // 2023-01-30
            "1, false", // 2023-01-30
            "0, false"  // 2023-01-30
        ]
    )
    fun shouldShowSectionTitleTest(index: Int, expected: Boolean) {
        val actual = shouldShowSectionTitle(testMessageList, index)

        assertEquals(actual, expected)
    }
}
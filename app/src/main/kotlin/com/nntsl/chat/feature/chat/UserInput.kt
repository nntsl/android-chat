package com.nntsl.chat.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {}
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    Row(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .defaultMinSize(minHeight = 48.dp),
            value = textState,
            singleLine = false,
            minLines = 1,
            maxLines = 6,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            onValueChange = {
                textState = it
            },
            shape = RoundedCornerShape(20.dp),
            textStyle = MaterialTheme.typography.bodyMedium
        )
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .alpha(if (textState.text.isNotEmpty()) 1f else 0.5f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            enabled = textState.text.isNotEmpty(),
            onClick = {
                onMessageSent(textState.text)
                textState = TextFieldValue()
                // Move scroll to bottom
                resetScroll()
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = "chat:send",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// User input block
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp),
        value = textFieldValue,
        singleLine = false,
        minLines = 1,
        maxLines = 6,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onValueChange = {
            onTextChanged(it)
        },
        shape = RoundedCornerShape(20.dp),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

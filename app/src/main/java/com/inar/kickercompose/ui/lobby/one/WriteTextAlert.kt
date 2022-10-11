package com.inar.kickercompose.ui.lobby.one

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.inar.kickercompose.data.viemodels.TestViewModel
import kotlinx.coroutines.launch

@Composable
fun WriteTextAlert(
    isOpen: Boolean,
    text: String,
    vm: TestViewModel,
    onDismiss: () -> Unit,
) {

    var message by remember { mutableStateOf(text) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()


    suspend fun sendMessage() {
        val res = vm.battle.changeMessageBattle(message)
        if (res.success) onDismiss()
        else errorMessage = res.message
    }

    if (isOpen) {
        AlertDialog(onDismissRequest = onDismiss,
            title = { Text(text = "Change lobby message") },
            text = { TextField(value = message, onValueChange = { message = it }) },
            buttons = {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Button(modifier = Modifier.fillMaxWidth(0.5f), onClick = {
                        scope.launch {
                            sendMessage()
                        }
                    }) {
                        Text(text = "Change message")
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                }


            }
        )
    }

}
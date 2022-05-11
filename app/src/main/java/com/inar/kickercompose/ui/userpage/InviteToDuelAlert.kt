package com.inar.kickercompose.ui.userpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.navigation.NavigationItems
import kotlinx.coroutines.launch

@Composable
fun InviteToDuelAlert(
    invitedId: String,
    isOpen: Boolean,
    vm: TestViewModel,
    navController: NavHostController,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    suspend fun sendInviteToDuel() {

        val res = vm.battle.createAndInvite(invitedId, message)
        if (res.success) {
            navController.strangeNavigate(NavigationItems.MyLobby.route)
            errorMessage = null
        } else errorMessage = res.message

    }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Challenge to a duel!", fontWeight = FontWeight.Bold) },
            text = {
                if (errorMessage != null) Text(text = errorMessage!!, color = Color.Red)
                TextField(value = message, onValueChange = { message = it })
            },
            buttons = {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Button(modifier = Modifier.fillMaxWidth(0.5f), onClick = {
                        scope.launch {
                            sendInviteToDuel()
                        }
                    }) {
                        Text(text = "Send")
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                }
            }
        )
    }


}
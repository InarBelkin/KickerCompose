package com.inar.kickercompose.ui.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.TestViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateLobbyAlert(
    isOpen: Boolean,
    vm: TestViewModel,
    navController: NavHostController,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Box(modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Create lobby", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.padding(7.dp)) {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                        onClick = {
                            scope.launch {
                                LobbyFuns.createLobbyButton(false,
                                    vm,
                                    navController,
                                    context)
                            }
                        }) {
                        Text(text = "1 vs 1")
                    }
                    Button(modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(), onClick = {
                        scope.launch {
                            LobbyFuns.createLobbyButton(true,
                                vm,
                                navController,
                                context)
                        }
                    }) {
                        Text(text = "2 vs 2")
                    }
                }
            },
            buttons = {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = "Cancel")
                    }
                }

            })
    }


}
package com.inar.kickercompose.ui.lobby.one

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
fun StartBattleAlert(
    isOpen: Boolean,
    vm: TestViewModel,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Box(modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Start battle", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(text = "Your lobby is not full! You can start battle, but results will not be included in statistics",
                    softWrap = true)
            },
            buttons = {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Button(modifier = Modifier.fillMaxWidth(0.5f), onClick = {
                        scope.launch {
                            vm.battle.startBattle()
                        }
                    }) {
                        Text(text = "Start!")
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                }
            }

        )
    }


}
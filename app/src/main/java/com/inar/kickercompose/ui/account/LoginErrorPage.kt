package com.inar.kickercompose.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inar.kickercompose.data.viemodels.TestViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginErrorPage(vm: TestViewModel) {
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.padding(7.dp)) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Connection error", fontSize = 18.sp)
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                scope.launch {
                    vm.account.loginRefresh()
                }
            }) {
                Text(text = "Reload")
            }
        }


    }

}
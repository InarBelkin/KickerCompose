package com.inar.kickercompose.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inar.kickercompose.data.models.account.RegisterDto
import com.inar.kickercompose.data.models.states.KeyboardState
import com.inar.kickercompose.data.models.states.MessageState
import com.inar.kickercompose.data.models.states.MessageStyle
import com.inar.kickercompose.data.models.states.keyboardAsState
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.navigation.AccountItems
import kotlinx.coroutines.launch

@Composable
fun RegistrationPage(vm: TestViewModel, navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isKeyboardOpen by keyboardAsState()
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf(MessageState("", MessageStyle.Nothing)) }
    val context = LocalContext.current

    val mainAlignment =
        if (isKeyboardOpen == KeyboardState.Opened) Alignment.TopCenter else Alignment.Center

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = mainAlignment) {
        Column(modifier = Modifier.width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message.message)

            TextField(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                placeholder = { Text("email") }
            )

            TextField(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
                value = login,
                onValueChange = { login = it },
                label = { Text("username") },
                placeholder = { Text("username") }
            )

            PassField(value = password, onValueChange = { password = it })

            Button(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(60.dp), onClick = {
                scope.launch {
                    message = vm.account.register(RegisterDto().apply {
                        this.name = login
                        this.email = email
                        this.password = password
                    })
                }
            }) {
                Text(text = "Registration")
            }

            Text(modifier = Modifier.clickable {
                navController.navigate(AccountItems.Login.route)
            }, text = "To login")

        }
    }
}
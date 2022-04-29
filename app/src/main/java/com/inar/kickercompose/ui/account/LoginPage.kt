package com.inar.kickercompose.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inar.kickercompose.data.models.account.LoginDto
import com.inar.kickercompose.data.models.states.KeyboardState
import com.inar.kickercompose.data.models.states.MessageState
import com.inar.kickercompose.data.models.states.MessageStyle
import com.inar.kickercompose.data.models.states.keyboardAsState
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.navigation.AccountItems
import kotlinx.coroutines.launch

@Composable
fun LoginPage(vm: TestViewModel, navController: NavController) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isKeyboardOpen by keyboardAsState()
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf(MessageState("", MessageStyle.Nothing)) }

    val mainAlignment =
        if (isKeyboardOpen == KeyboardState.Opened) Alignment.TopCenter else Alignment.Center


    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = mainAlignment) {

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

            PassField(value = password) {
                password = it
            }

            Button(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(60.dp), onClick = {
                scope.launch {
                    message = vm.account.login(LoginDto().apply {
                        this.email = email
                        this.password = password
                    })
                }
            }) {
                Text(text = "Login")
            }

            Text(modifier = Modifier.clickable {
                navController.navigate(AccountItems.Registration.route)
            }, text = "To registration")


        }
    }
}

@Composable
fun PassField(value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text("password") },
        placeholder = { Text("password") },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            val description: String =
                if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )

}
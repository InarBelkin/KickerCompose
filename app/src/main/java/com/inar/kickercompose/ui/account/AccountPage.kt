package com.inar.kickercompose.ui.account

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inar.kickercompose.data.models.account.LoginDto
import com.inar.kickercompose.data.models.states.KeyboardState
import com.inar.kickercompose.data.models.states.keyboardAsState
import com.inar.kickercompose.ui.AccountItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AccountPage(accountVM: AccountViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AccountItems.Login.route) {

        composable(AccountItems.Login.route) {
            LoginPage(accountVM, navController)
        }

        composable(AccountItems.Registration.route) {
            RegistrationPage(accountVM)
        }

    }
}

@Composable
fun LoginPage(accountVM: AccountViewModel, navController: NavController) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val isKeyboardOpen by keyboardAsState()
    val scope = rememberCoroutineScope()

    val mainAlignment =
        if (isKeyboardOpen == KeyboardState.Opened) Alignment.TopCenter else Alignment.Center

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = mainAlignment) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = email, onValueChange = { email = it },
                label = { Text("email") },
                placeholder = { Text("email") }
            )

            TextField(
                value = password,
                onValueChange = { password = it },
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

            Button(onClick = {
                scope.launch {
                    accountVM.login(context, LoginDto().apply {
                        this.email = email
                        this.password = password
                    })
                }
            }) {

            }
        }
    }
}

@Composable
fun RegistrationPage(accountVM: AccountViewModel) {

}
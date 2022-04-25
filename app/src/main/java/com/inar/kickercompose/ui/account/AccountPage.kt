package com.inar.kickercompose.ui.account

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inar.kickercompose.ui.navigation.AccountItems


@Composable
fun AccountPage(accountVM: AccountViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AccountItems.Login.route) {

        composable(AccountItems.Login.route) {
            LoginPage(accountVM, navController)
        }

        composable(AccountItems.Registration.route) {
            RegistrationPage(accountVM, navController)
        }

    }
}


package com.inar.kickercompose.ui.account

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.navigation.AccountItems


@Composable
fun AccountPage(vm: TestViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AccountItems.Login.route) {

        composable(AccountItems.Login.route) {
            LoginPage(vm, navController)
        }

        composable(AccountItems.Registration.route) {
            RegistrationPage(vm, navController)
        }

    }
}


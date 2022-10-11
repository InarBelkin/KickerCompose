package com.inar.kickercompose.other

import androidx.navigation.NavController

fun NavController.strangeNavigate(route: String) {
    this.navigate(route) {
        popUpTo(route) {
            inclusive = true;
        }
        launchSingleTop = true;
    }

}
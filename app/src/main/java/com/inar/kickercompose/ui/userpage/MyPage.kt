package com.inar.kickercompose.ui.userpage

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.StrangeUtils

@Composable
fun MyPage(vm: TestViewModel, navHostController: NavHostController) {
    var userId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val t = vm.account.getRefreshToken()!!
        userId = StrangeUtils.decodeJwt(t);
    }

    UserPage(vm = vm, userId = userId, navHostController)


}


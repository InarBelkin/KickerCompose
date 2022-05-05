package com.inar.kickercompose.ui.userpage

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.StrangeUtils

@Composable
fun MyPage(vm: TestViewModel, navHostController: NavHostController) {

    UserPage(vm = vm, navHostController) {
        val t = vm.account.getRefreshToken()!!
        val userId = StrangeUtils.decodeJwt(t)
        vm.loadUserDetails(userId)
    }


}


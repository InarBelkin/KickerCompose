package com.inar.kickercompose.ui.userpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.navigation.NavigationItems
import kotlinx.coroutines.launch

@Composable
fun SettingsPage(vm: TestViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(7.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "My page", fontSize = 18.sp)
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                scope.launch {
                    vm.account.logOut()
                    // navController.strangeNavigate(NavigationItems.Lo)
                }
            }) {
                Text(text = "LogOut")
            }
        }


    }
}


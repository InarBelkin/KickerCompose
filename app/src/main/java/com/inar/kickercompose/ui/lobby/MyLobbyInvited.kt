package com.inar.kickercompose.ui.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.viemodels.TestViewModel

@Composable
fun MyLobbyInvited(lobby: LobbyItemModel, vm: TestViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {

    }


}
package com.inar.kickercompose.ui.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.navigation.NavigationItems
import kotlinx.coroutines.launch

@Composable
fun MyLobbyInvited(lobby: LobbyItemModel, vm: TestViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Initiator - ${lobby.initiator.name}", softWrap = true)
            Text(text = lobby.message, softWrap = true, fontStyle = FontStyle.Italic)
            Text(text = "Side A")
            for (position in 0 until lobby.sideA.count()) {
                FuckingUserInMyLobby(lobby.sideA[position], isLongPress = true) {
                }
            }

            for (position in 0 until lobby.sideB.count()) {
                FuckingUserInMyLobby(lobby.sideB[position], isLongPress = true) {
                }
            }

            Button(onClick = {
                scope.launch {
                    LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
                }
            }) {
                Text(text = "Leave this battle!")
            }
        }

    }


}

suspend fun LobbyFuns.leaveBattle(
    initiatorId: String,
    vm: TestViewModel,
    navController: NavHostController,
) {
    vm.battle.leaveBattle(LeaveBattleDto().also {
        it.initiatorId = initiatorId
        it.invitedId = vm.account.getUserClaims()?.id ?: ""
    })
    navController.strangeNavigate(NavigationItems.Lobby.route)
}
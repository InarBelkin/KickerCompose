package com.inar.kickercompose.ui.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.lobby.BattleStatus
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.messages.LeaveBattleDto
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.lobby.one.MyLobbyScreens
import com.inar.kickercompose.ui.navigation.NavigationItems
import com.inar.kickercompose.ui.theme.KickerColors
import com.inar.kickercompose.ui.theme.utils.BorderedButton
import kotlinx.coroutines.delay
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

            when (lobby.lastTimeStamp?.battleState ?: BattleStatus.Created) {
                BattleStatus.Created.num -> MyLobbyInvitedScreens.Created(lobby, vm, navController)
                BattleStatus.Started.num -> MyLobbyInvitedScreens.Started(lobby, vm, navController)
                BattleStatus.Paused.num -> MyLobbyInvitedScreens.Paused(lobby, vm, navController)
                BattleStatus.EnteringResults.num -> MyLobbyInvitedScreens.EnteringResults(lobby,
                    vm,
                    navController)
                BattleStatus.Ended.num -> MyLobbyInvitedScreens.Ended()
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

object MyLobbyInvitedScreens {
    @Composable
    fun Created(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val scope = rememberCoroutineScope()

        BorderedButton(text = "Leave this battle!", color = KickerColors.dangerous, isLongPress = true) {
            scope.launch {
                LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
            }
        }

//        Button(onClick = {
//            scope.launch {
//                LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
//            }
//        }) {
//            Text(text = "Leave this battle!")
//        }
    }

    @Composable
    fun Started(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var time by remember { mutableStateOf(lobby.lastTimeStamp?.getCurrentBattleTime() ?: 0.0) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(100)
                time += 0.1
            }
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = String.format("%.1f", time), fontSize = 40.sp)


            BorderedButton(text = "Leave this battle!", color = KickerColors.dangerous, isLongPress = true) {
                scope.launch {
                    LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
                }
            }

//            Button(onClick = {
//                scope.launch {
//                    LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
//                }
//            }) {
//                Text(text = "Leave this battle!")
//            }
        }
    }

    @Composable
    fun Paused(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val scope = rememberCoroutineScope()
        Text(text = String.format("%.1f", lobby.lastTimeStamp?.battleTime ?: 0),
            fontSize = 40.sp)

        BorderedButton(text = "Leave this battle!", color = KickerColors.dangerous, isLongPress = true) {
            scope.launch {
                LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
            }
        }
//        Button(onClick = {
//            scope.launch {
//                LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
//            }
//        }) {
//            Text(text = "Leave this battle!")
//        }
    }

    @Composable
    fun EnteringResults(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val scope = rememberCoroutineScope()
        Text(text = String.format("%.1f", lobby.lastTimeStamp?.battleTime ?: 0),
            fontSize = 40.sp)

        BorderedButton(text = "Leave this battle!", color = KickerColors.dangerous, isLongPress = true) {
            scope.launch {
                LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
            }
        }
//        Button(onClick = {
//            scope.launch {
//                LobbyFuns.leaveBattle(lobby.initiator.id!!, vm, navController);
//            }
//        }) {
//            Text(text = "Leave this battle!")
//        }
    }

    @Composable
    fun Ended(
    ) {
        Text(text = "Battle is ended. Nothing to see here!")

    }

}

package com.inar.kickercompose.ui.lobby

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.lobby.IsAccepted
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.LobbyUserShortInfo
import com.inar.kickercompose.data.models.lobby.messages.InviteAnswer
import com.inar.kickercompose.data.models.states.loadstates.LoadScreen
import com.inar.kickercompose.data.viemodels.LobbyGuestVm
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.navigation.NavigationItems
import kotlinx.coroutines.launch

@Composable
fun LobbyGuestPage(initiatorId: String, vm: TestViewModel, navController: NavHostController) {
    val guestLobbyVm: LobbyGuestVm = hiltViewModel();
    val guestLobby by guestLobbyVm.guestLobbyLd.observeAsState()

    LaunchedEffect(initiatorId) {
        guestLobbyVm.reloadLobby(initiatorId)
    }

    LoadScreen(loadedContent = guestLobby!!) {
        Box(modifier = Modifier.padding(7.dp)) {
            LobbyGuest(lobby = guestLobby!!.value, vm = vm, navController = navController)
        }
    }


}

@Composable
fun LobbyGuest(lobby: LobbyItemModel, vm: TestViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Initiator - ${lobby.initiator.name}", softWrap = true)

        Text(text = lobby.message, softWrap = true, fontStyle = FontStyle.Italic)

        Text(text = "Long press to join the fight:")

        Text(text = "Side A")
        for (position in 0 until lobby.sideA.count()) {
            FuckingUserInMyLobby(lobby.sideA[position], isLongPress = true) {
                scope.launch {
                    LobbyFuns.acceptInviteFromGuest(
                        lobby.sideA[position],
                        vm,
                        navController,
                        lobby.initiator.id ?: "",
                        0,
                        position
                    )
                }
            }
        }

        Text(text = "Side B")
        for (position in 0 until lobby.sideB.count()) {
            FuckingUserInMyLobby(lobby.sideB[position], isLongPress = true) {
                scope.launch {
                    LobbyFuns.acceptInviteFromGuest(
                        lobby.sideB[position],
                        vm,
                        navController,
                        lobby.initiator.id ?: "",
                        1,
                        position
                    )
                }
            }
        }


    }
}

suspend fun LobbyFuns.acceptInviteFromGuest(
    user: LobbyUserShortInfo,
    vm: TestViewModel,
    navController: NavHostController,
    initiatorId: String,
    side: Int,
    position: Int,
) {
    if (vm.battle.myLobbyLd.value!!.value == null) {
        val userId = vm.account.getUserClaims()?.id ?: ""
        if (user.accepted == IsAccepted.Invited.a && user.id == vm.account.getUserClaims()!!.id ||
            user.accepted == IsAccepted.AllInvited.a
        ) {
            vm.battle.sendInviteAnswerFromGuest(InviteAnswer().also {
                it.invitedId = userId
                it.initiatorId = initiatorId
                it.accepted = true
                it.side = side
                it.position = position
            })
            navController.strangeNavigate(NavigationItems.MyLobby.route)
        }
    }


}
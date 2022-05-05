package com.inar.kickercompose.ui.lobby

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.leaderboard.FrameWithBorder
import com.inar.kickercompose.ui.leaderboard.LeaderboardItem
import kotlinx.coroutines.launch

@Composable
fun InviteInLobby(vm: TestViewModel, navController: NavHostController, side: Int, position: Int) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val users by vm.leaderboardLd.observeAsState()
    var myId by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        vm.loadLeaderboard();
        myId = vm.account.getUserClaims()?.id ?: ""
    }
    Box(modifier = Modifier.padding(7.dp)) {
        Column() {

            if (kickFilter(vm, side, position)) {
                FrameWithBorder(onClick = {
                    scope.launch {
                        LobbyFuns.kickUser(context,
                            vm,
                            navController,
                            side,
                            position)
                    }
                }) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "Kick the player")
                    }
                }
            }
            if (meFilter(myId, vm)) {
                FrameWithBorder(onClick = {
                    scope.launch {
                        addMe(context, vm,
                            navController,
                            side,
                            position)
                    }
                }) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "Add myself")
                    }
                }
            }
            FrameWithBorder(onClick = {
                scope.launch {
                    LobbyFuns.inviteAllUsers(context, vm, navController, side, position)
                }
            }) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Invite All")
                }
            }

            Text(text = "Invite User:")

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(users!!.value.data.filter { it.id != myId && lobbyFilter(it.id, vm) })
                { _, item ->
                    LeaderboardItem(user = item) {
                        scope.launch {
                            LobbyFuns.inviteOne(context, vm,
                                navController,
                                item.id,
                                side,
                                position)
                        }
                    }
                }
            }
        }
    }
}

fun kickFilter(vm: TestViewModel, side: Int, position: Int): Boolean {
    val l = vm.battle.myLobbyLd.value!!.value!!
    val item = if (side == 0) l.sideA[position]
    else l.sideB[position]
    return item.id != null
}

fun lobbyFilter(userId: String, vm: TestViewModel): Boolean {
    if (vm.battle.myLobbyLd.value!!.value == null) return false
    val sideA = vm.battle.myLobbyLd.value!!.value?.sideA!!
    val sideB = vm.battle.myLobbyLd.value!!.value?.sideB!!
    val count = listOf(*sideA.toTypedArray(),
        *sideB.toTypedArray()).filter { it.id == userId && it.accepted in arrayOf(1, 2) }.count()
    return count == 0
}

fun meFilter(myId: String, vm: TestViewModel): Boolean {
    if (vm.battle.myLobbyLd.value!!.value == null) return false
    val sideA = vm.battle.myLobbyLd.value!!.value?.sideA!!
    val sideB = vm.battle.myLobbyLd.value!!.value?.sideB!!
    val count = listOf(*sideA.toTypedArray(),
        *sideB.toTypedArray()).filter { it.id == myId }.count()
    return count == 0
}
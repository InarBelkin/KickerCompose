package com.inar.kickercompose.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.lobby.IsAccepted
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.LobbyUserShortInfo
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.lobby.CreateLobbyAlert
import com.inar.kickercompose.ui.lobby.LobbyFuns
import com.inar.kickercompose.ui.lobby.toMyBattle
import kotlinx.coroutines.launch

@Composable
fun Lobby(vm: TestViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val lobbys by vm.battle.lobbyLd.observeAsState()
    val myLobby by vm.battle.myLobbyLd.observeAsState()
    val scope = rememberCoroutineScope()
    var isCreateBattleDialogOpen by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        vm.battle.loadLobby()
        vm.battle.loadMyLobby()
    }
    DisposableEffect(context) {
        vm.battle.observeLobbyChanges(context)
        onDispose {
            vm.battle.disposeObserveLobbyChanges(context)
        }
    }


    Box(modifier = Modifier.padding(7.dp)) {
        Scaffold(bottomBar = {
            if (myLobby!! is LoadedState.Success && myLobby!!.value != null) {
                Button(modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth(), onClick = { toMyBattle(vm, navController) }) {
                    Text("To current battle!")
                }
            } else if (lobbys!! is LoadedState.Success && myLobby!!.value == null) {
                Button(modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth(), onClick = {
                    isCreateBattleDialogOpen = true
                }) {
                    Text("Create battle!")
                }
            }

        }) {
            Box(modifier = Modifier.padding(it)) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(lobbys!!.value) { _, item ->
                        LobbyItem(lobby = item, vm) {
                            LobbyFuns.lobbyListClick(item, vm, navController)
                        }
                    }
                }
            }
        }
    }

    CreateLobbyAlert(isOpen = isCreateBattleDialogOpen, vm, navController) {
        isCreateBattleDialogOpen = false
    }

    BottomLoadOverlay(lobbys!!)
}

@Composable
fun LobbyItem(lobby: LobbyItemModel, vm: TestViewModel, onClick: () -> Unit) {
    var isMy by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val myid = vm.account.getUserClaims()?.id
        val count = listOf(*lobby.sideA.toTypedArray(),
            *lobby.sideB.toTypedArray(),
            lobby.initiator).filter { it.id == myid }.count()
        isMy = count > 0
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp)
        .clickable {
            onClick.invoke()
        },
        border = BorderStroke(2.dp,
            if (isMy) MaterialTheme.colors.secondary else
                MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp)) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Battle of ${lobby.initiator.name}")
            Text(text = lobby.message, fontStyle = FontStyle.Italic)
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(text = "Side A")
                    for (user in lobby.sideA) {
                        UserShortItem(user, vm)
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Side B")
                    for (user in lobby.sideB) {
                        UserShortItem(user, vm)
                    }
                }
            }
        }

    }
}

@Composable
fun UserShortItem(user: LobbyUserShortInfo, vm: TestViewModel) {
    var isMy by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val myid = vm.account.getUserClaims()?.id
        isMy = (user.id == myid)
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp),
        border = BorderStroke(2.dp,
            if (isMy) MaterialTheme.colors.secondary else MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp)) {

        Row() {
            Image(
                painter = painterResource(id = R.drawable.cat_pic),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = user.name, softWrap = true)
                Text(text = IsAccepted.fromInt(user.accepted).description)
            }
        }
    }


}

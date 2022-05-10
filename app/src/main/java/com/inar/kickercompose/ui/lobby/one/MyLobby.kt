package com.inar.kickercompose.ui.lobby

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.lobby.*
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.lobby.one.MyLobbyScreens
import com.inar.kickercompose.ui.navigation.NavigationItems
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.NullPointerException


@Composable
fun MyLobby(vm: TestViewModel, navController: NavHostController) {
    val mylobby by vm.battle.myLobbyLd.observeAsState()
    var iAmInitiator by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {

        vm.battle.loadMyLobby()


        try {
            if (mylobby!!.value != null && mylobby!!.value!!.initiator.id == vm.account.getUserClaims()!!.id) {
                iAmInitiator = true
            }
        } catch (e: NullPointerException) {
        }
    }

    DisposableEffect(context) {
        vm.battle.observeLobbyDeleted(context) { i, m ->
            if (!i) navController.strangeNavigate(NavigationItems.Lobby.route)
            else navController.strangeNavigate(NavigationItems.BattleResult.clearRoute + m)
        }
        onDispose {
        //    vm.battle.disposeObserveDeleted(context)
        }
    }



    Box(modifier = Modifier
        .padding(7.dp)
        .verticalScroll(rememberScrollState())) {
        when {
            iAmInitiator && mylobby!!.value != null -> {
                LobbyIAmInitiator(lobby = mylobby!!.value!!, vm, navController)
            }
            mylobby!!.value != null -> {
                MyLobbyInvited(mylobby!!.value!!, vm, navController)
            }
            else -> EmptyLobby()
        }
    }

    BottomLoadOverlay(mylobby!!)
}

@Composable
fun EmptyLobby() {
    Text(text = "Your lobby doesn't exists")
}


@Composable
fun LobbyIAmInitiator(lobby: LobbyItemModel, vm: TestViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Initiator - You, ${lobby.initiator.name}", softWrap = true)
        TextField(value = lobby.message, onValueChange = {

        })
        Text(text = "Side A")
        for (position in 0 until lobby.sideA.count()) {
            FuckingUserInMyLobby(lobby.sideA[position]) {
                openInviteWin(navController, 0, position)
            }
        }
        Text(text = "Side B")
        for (position in 0 until lobby.sideB.count()) {
            FuckingUserInMyLobby(lobby.sideB[position]) {
                openInviteWin(navController, 1, position)
            }
        }

        Text(text = "Battle is ${BattleStatus.fromInt(lobby.lastTimeStamp?.battleState ?: 0).description}")

        when (lobby.lastTimeStamp?.battleState ?: BattleStatus.Created) {
            BattleStatus.Created.num -> MyLobbyScreens.Created(vm, navController)
            BattleStatus.Started.num -> MyLobbyScreens.Started(lobby, vm, navController)
            BattleStatus.Paused.num -> MyLobbyScreens.Paused(lobby, vm, navController)
            BattleStatus.EnteringResults.num -> MyLobbyScreens.EnteringResults(lobby,
                vm,
                navController)
            BattleStatus.Ended.num -> MyLobbyScreens.Ended(lobby, vm, navController)
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FuckingUserInMyLobby(
    user: LobbyUserShortInfo,
    isLongPress: Boolean = false,
    onClick: () -> Unit,
) {
    LaunchedEffect(Unit) {
    }
    val cardHeight = 60.dp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .height(cardHeight)
            .combinedClickable(
                onClick = { if (!isLongPress) onClick() },
                onLongClick = { if (isLongPress) onClick() }),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp),
    ) {

        Box(contentAlignment = Alignment.CenterStart) {
            if (user.id == null) {
                Text(text = "This place is empty")
            } else {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.cat_pic),
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)

                    )
                    Column(
                        modifier = Modifier.height(cardHeight),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = user.name)
                        Text(text = "ELO:${user.elo}")
                    }
                }
            }
        }

        Box(contentAlignment = Alignment.CenterEnd) {
            Column() {
                Text(text = "Role = ${Role.fromInt(user.role).description}")
                Text(text = "Status = ${IsAccepted.fromInt(user.accepted).description}")
            }

        }


    }
}
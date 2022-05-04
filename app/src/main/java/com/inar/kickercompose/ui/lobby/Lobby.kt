package com.inar.kickercompose.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.LobbyUserShortInfo
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.lobby.createLobbyButton
import com.inar.kickercompose.ui.lobby.toMyBattle
import kotlinx.coroutines.launch

@Composable
fun Lobby(vm: TestViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val lobbys by vm.battle.lobbyLd.observeAsState()
    val myLobby by vm.battle.myLobbyLd.observeAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        vm.battle.loadLobby()
        vm.battle.loadMyLobby()
    }

    Box(modifier = Modifier.padding(7.dp)) {
        Scaffold(bottomBar = {
            if (myLobby!! is LoadedState.Success && myLobby!!.value != null) {
                Button(modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth(), onClick = { scope.launch { toMyBattle(vm, navController) } }) {
                    Text("To current battle!")
                }
            } else if (lobbys!! is LoadedState.Success) {
                Button(modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth(), onClick = {
                    scope.launch {
                        createLobbyButton(vm, navController, context)
                    }
                }) {
                    Text("Create battle!")
                }
            }

        }) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(lobbys!!.value) { _, item ->
                    LobbyItem(lobby = item)
                }
            }
        }
    }
}

@Composable
fun LobbyItem(lobby: LobbyItemModel) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp)) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Battle of ${lobby.initiator.name}")
            Text(text = lobby.message, fontStyle = FontStyle.Italic)
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(text = "Side A")
                    for (user in lobby.sideA) {
                        UserShortItem(user)
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Side B")
                    for (user in lobby.sideB) {
                        UserShortItem(user)
                    }
                }
            }
        }

    }
}

@Composable
fun UserShortItem(user: LobbyUserShortInfo?) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp)) {
        if (user != null) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.cat_pic),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Column(verticalArrangement = Arrangement.Center) {
                    Text(text = user.name)
                    Text(text = user.elo.toString())
                }
            }
        } else {
            Box(modifier = Modifier.height(40.dp), contentAlignment = Alignment.Center) {
                Text(text = "This place is empty",
                    style = TextStyle(fontFamily = FontFamily.Cursive))
            }
        }

    }

}

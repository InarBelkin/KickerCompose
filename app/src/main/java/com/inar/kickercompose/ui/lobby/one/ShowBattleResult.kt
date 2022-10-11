package com.inar.kickercompose.ui.lobby.one

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.data.viemodels.BattleEndVm
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.lobby.FuckingUserInMyLobby
import com.inar.kickercompose.ui.lobby.LobbyFuns
import com.inar.kickercompose.ui.lobby.leaveBattle
import com.inar.kickercompose.ui.navigation.NavigationItems
import com.inar.kickercompose.ui.theme.KickerColors
import com.inar.kickercompose.ui.theme.utils.BorderedButton
import kotlinx.coroutines.launch

@Composable
fun ShowBattleResults(battleId: String, navHostController: NavHostController) {
    val TAG = "ShowBattleResults"
    val bvm: BattleEndVm = hiltViewModel();
    val battle by bvm.battleResultLd.observeAsState();

    val isStrange =
        battle!!.value.sideA.count() != battle!!.value.sideB.count() || battle!!.value.sideA.isEmpty() || battle!!.value.sideB.isEmpty()

    LaunchedEffect(battleId) {
        bvm.reloadBattleResult(battleId)
        Log.d(TAG, "get it!")
    }

    Box(modifier = Modifier.padding(7.dp), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Battle ended", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            if (isStrange) Text(text = "That was very strange battle...")
            Text(text = "Winners:")
            Text(text = "(just look at these beauties!)")
            val isWinnerA = battle!!.value.result.isWinnerA;


            for (u in when (isWinnerA) {
                true -> battle!!.value.sideA
                false -> battle!!.value.sideB
                else -> listOf()
            }) {
                FuckingUserInMyLobby(user = u, showStatus = false) {
                }
            }

            Text(text = "Losers:")
            Text(text = "(good luck in next time)")
            for (u in when (isWinnerA) {
                true -> battle!!.value.sideB
                false -> battle!!.value.sideA
                else -> listOf()
            }) {
                FuckingUserInMyLobby(user = u, showStatus = false) {
                }
            }

            BorderedButton(text = "to lobby list") {
                navHostController.strangeNavigate(NavigationItems.Lobby.route)
            }

//            Button(onClick = { navHostController.strangeNavigate(NavigationItems.Lobby.route) }) {
//                Text(text = "to lobby list")
//            }
        }

    }

    BottomLoadOverlay(battle!!)
}
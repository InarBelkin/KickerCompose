package com.inar.kickercompose.ui.lobby.one

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.BattleEndVm
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.lobby.FuckingUserInMyLobby
import com.inar.kickercompose.ui.navigation.NavigationItems

@Composable
fun ShowBattleResults(battleId: String, navHostController: NavHostController) {
    val bvm: BattleEndVm = hiltViewModel();
    val battle by bvm.battleResultLd.observeAsState();

    LaunchedEffect(battleId) {
        bvm.reloadBattleResult(battleId);
    }

    Box(modifier = Modifier.padding(7.dp)) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Battle ended")
            Text(text = "Winners:")
            Text(text = "(just look at these beauties!)")
            val isWinnerA = battle!!.value.result.isWinnerA;

            for (u in when (isWinnerA) {
                true -> battle!!.value.sideA
                false -> battle!!.value.sideB
                else -> listOf()
            }) {
                FuckingUserInMyLobby(user = u) {
                }
            }

            Text(text = "Losers:")
            Text(text = "(good luck in next time)")
            for (u in when (isWinnerA) {
                true -> battle!!.value.sideB
                false -> battle!!.value.sideA
                else -> listOf()
            }) {
                FuckingUserInMyLobby(user = u) {
                }
            }

            Button(onClick = { navHostController.strangeNavigate(NavigationItems.Lobby.route) }) {
                Text(text = "to lobby list")
            }
        }

    }


}
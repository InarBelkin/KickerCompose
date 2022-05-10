package com.inar.kickercompose.ui.lobby.one

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.viemodels.BattleEndVm
import com.inar.kickercompose.data.viemodels.TestViewModel

@Composable
fun ShowBattleResults(battleId: String, navHostController: NavHostController) {
    val bvm: BattleEndVm = hiltViewModel();
    val battle by bvm.battleResultLd.observeAsState();

    LaunchedEffect(battleId) {
        bvm.reloadBattleResult(battleId);
    }


}
package com.inar.kickercompose.ui.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.ui.TestViewModel

@Composable
fun Leaderboard(vm: TestViewModel, navController: NavHostController) {
    val users by vm.leaderboardLd.observeAsState()
    LaunchedEffect(Unit) {
        vm.loadLeaderboard();
    }

    LazyColumn(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxSize()) {
        itemsIndexed(users!!.value.data) { _, item ->
            LeaderboardItem(user = item, navController = navController)
        }
    }

    BottomLoadOverlay(users!!)
}



package com.inar.kickercompose.ui.userpage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.ui.TestViewModel
import com.inar.kickercompose.ui.leaderboard.LeaderboardItem

@Composable
fun UserPage(vm: TestViewModel, userId: String) {

    val user by vm.userDetailsLiveData.observeAsState()
    var isOneVsOneSelected by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(userId) {
        vm.loadUserDetails(userId)
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LeaderboardItem(user = user!!.value.toUserLeaderboard())
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                Text(text = "Challenge to a duel!")
            }
        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(40.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .background(if (isOneVsOneSelected) MaterialTheme.colors.secondary
                    else MaterialTheme.colors.primaryVariant)
                    .clickable {
                        isOneVsOneSelected = true
                    },
                    contentAlignment = Alignment.Center) {
                    Text(text = "1 vs 1")
                }
                Box(modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()
                    .background(if (isOneVsOneSelected) MaterialTheme.colors.primaryVariant
                    else MaterialTheme.colors.secondary)
                    .clickable { isOneVsOneSelected = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "2 vs 2")
                }
            }
        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)) {
            if (isOneVsOneSelected) {
                StatsCard(stats = user!!.value.statsOneVsOneList)
            } else {
                StatsCard(stats = user!!.value.statsTwoVsTwoList)
            }
        }

        Text(text = userId)
    }

    BottomLoadOverlay(user!!)
}

@Composable
fun StatsCard(stats: List<Pair<String, String>>) {
    Column(modifier = Modifier.padding(10.dp)) {
        stats.forEach {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart) {
                    Text(text = it.first)
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(text = it.second)
                }
            }
        }
    }
}


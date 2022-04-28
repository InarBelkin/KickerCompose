package com.inar.kickercompose.ui.userpage

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.inar.kickercompose.data.models.lobby.InviteRequestDto
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.data.models.userdetails.UserDetails
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.leaderboard.LeaderboardItem
import kotlinx.coroutines.launch

@Composable
fun UserPage(vm: TestViewModel, userId: String) {

    val user by vm.userDetailsLiveData.observeAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        vm.loadUserDetails(userId)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(7.dp)) {
        if (user!!.value.isMe) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                LeaderboardItem(user = user!!.value.toUserLeaderboard())
                StatsCardSelector(user = user!!.value)
                Text(text = user!!.value.isMe.toString())

            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                StrokePseudoButton(text = "Settings") { }
            }

        } else {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                LeaderboardItem(user = user!!.value.toUserLeaderboard())
                StrokePseudoButton(text = "Challenge to a duel!") {
                    scope.launch {
                        vm.hub.inviteOne(InviteRequestDto().apply {
                            invitedId = user!!.value.id
                            message = "mil zhopu"
                        });

                    }
                }
                StatsCardSelector(user = user!!.value)
                Text(text = user!!.value.isMe.toString())
            }
        }
    }

    BottomLoadOverlay(user!!)
}

@Composable
fun StatsCardSelector(user: UserDetails) {
    var isOneVsOneSelected by rememberSaveable { mutableStateOf(true) }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp)
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
        .padding(7.dp), shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)) {
        if (isOneVsOneSelected) {
            StatsCard(stats = user.statsOneVsOneList)
        } else {
            StatsCard(stats = user.statsTwoVsTwoList)
        }
    }
}

@Composable
fun StrokePseudoButton(
    text: String,
    strokeColor: Color = MaterialTheme.colors.primary,
    execution: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .clickable { execution.invoke() },
        border = BorderStroke(2.dp, strokeColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
            Text(text = text, color = strokeColor)
        }
    }
}

@Composable
fun StatsCard(stats: List<Pair<String, String>>) {
    Column(modifier = Modifier.padding(7.dp)) {
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


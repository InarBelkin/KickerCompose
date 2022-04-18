package com.inar.kickercompose.ui.userpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inar.kickercompose.other.loadstates.BottomLoadOverlay
import com.inar.kickercompose.ui.TestViewModel
import com.inar.kickercompose.ui.leaderboard.LeaderboardItem
import okhttp3.Challenge
import org.intellij.lang.annotations.JdkConstants

@Composable
fun UserPage(vm: TestViewModel, userId: String) {

    val user by vm.userDetailsLiveData.observeAsState()

    LaunchedEffect(userId) {
        vm.loadUserDetails(userId);
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LeaderboardItem(user = user!!.value.toUserLeaderboard())
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Challenge to a duel!")
        }
        Text(text = userId)
    }

    BottomLoadOverlay(user!!)
}
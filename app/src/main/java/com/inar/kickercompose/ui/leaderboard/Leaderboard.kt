package com.inar.kickercompose.ui.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inar.kickercompose.data.models.UserLeaderboard
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.LeaderboardWrapper
import com.inar.kickercompose.data.models.LoadedState
import com.inar.kickercompose.ui.TestViewModel

@Composable
fun Leaderboard(vm: TestViewModel) {
    val users by vm.leaderboardLd.observeAsState()
    LaunchedEffect(Unit) {
        vm.loadLeaderboard();
    }

    LazyColumn(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxSize()) {
        itemsIndexed(users!!.value.data) { _, item ->
            LeaderboardItem(user = item)
        }
    }

    when (users) {
        is LoadedState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Text(text = "loading...")
            }
        }
        is LoadedState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Text(text = "Error: ${(users as LoadedState.Error<LeaderboardWrapper>).error.message}")
            }
        }
        else -> {}
    }
}

@Composable
@Preview
fun LeaderboardItemPreview() {
    LeaderboardItem(user = UserLeaderboard().apply {
        name = "inar"
        this.elo = 100
    })
}

@Composable
fun LeaderboardItem(user: UserLeaderboard) {
    val cardHeight = 60.dp
    val textSize = 14.sp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(cardHeight),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp)
    ) {

        Box(contentAlignment = Alignment.CenterStart) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "#1", fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.cat_pic),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)

                )
            }

        }
        Box(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.height(cardHeight),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = user.name)
                Text(text = "ELO:${user.elo}")
            }
        }

        Box(contentAlignment = Alignment.CenterEnd) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .height(cardHeight),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "B:${user.countOfBattles}")
                Text(text = "V:${user.winsCount}")
            }
        }

    }

}
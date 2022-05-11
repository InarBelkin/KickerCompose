package com.inar.kickercompose.ui.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.inar.kickercompose.R
import com.inar.kickercompose.data.models.UserLeaderboard
import com.inar.kickercompose.data.models.lobby.BattleStatus
import com.inar.kickercompose.data.models.lobby.UserStatus
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.navigation.NavigationItems
import com.inar.kickercompose.ui.theme.Bronze
import com.inar.kickercompose.ui.theme.Gold
import com.inar.kickercompose.ui.theme.Silver

@Composable
@Preview
fun LeaderboardItemPreview() {
    LeaderboardItem(user = UserLeaderboard().apply {
        name = "inar"
        this.elo = 100
        this.status = UserStatus.Online.num
    })
}

@Composable
fun LeaderboardItem(user: UserLeaderboard, onClick: () -> Unit = {}) {
    val cardHeight = 60.dp

    var color = when {
        user.isMe -> MaterialTheme.colors.secondary
        user.stPlace == 1 -> Gold
        user.stPlace == 2 -> Silver
        user.stPlace == 3 -> Bronze

        else -> MaterialTheme.colors.primary
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .height(cardHeight)
            .clickable {
                onClick.invoke()
            },
        border = BorderStroke(2.dp, color),
        shape = RoundedCornerShape(15.dp),
    ) {

        Box(contentAlignment = Alignment.CenterStart) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                if (user.stPlace != 0) {
                    Text(
                        text = "#${user.stPlace}", fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Box(modifier = Modifier.size(40.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.cat_pic),
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)

                    )
                    Box(modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.BottomEnd) {
                        OnlineIcon(userStatus = user.status)
                    }
                }
            }

        }
        Box(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.height(cardHeight),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = user.name)
                Text(text = "ELO:${user.elo}")
            }
        }

        Box(contentAlignment = Alignment.CenterEnd) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .height(cardHeight),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                val fontSize = 14.sp
                Text(text = "B:${user.countOfBattles}", fontSize = fontSize)
                Text(text = "V:${user.winsCount}", fontSize = fontSize)
//                Text(text = UserStatus.fromInt(user.status).description,
//                    fontStyle = FontStyle.Italic, fontSize = fontSize)
            }
        }

    }

}

@Composable
fun OnlineIcon(userStatus: Int) {
    when (userStatus) {
        UserStatus.Online.num -> {
            Canvas(modifier = Modifier.size(10.dp), onDraw = {
                drawCircle(color = Color.Gray)
                drawCircle(color = Color.Green, radius = 9.5f)
            })
        }
        UserStatus.InBattle.num -> {
            val blueColor = MaterialTheme.colors.primaryVariant
            Canvas(modifier = Modifier.size(10.dp), onDraw = {
                drawCircle(color = Color.Gray)
                drawCircle(color = blueColor, radius = 9.5f)
            })
        }
    }
}

@Composable
fun FrameWithBorder(onClick: () -> Unit = {}, content: @Composable () -> Unit) {
    val cardHeight = 60.dp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .height(cardHeight)
            .clickable {
                onClick.invoke()
            },
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp),
    ) {
        content()
    }
}
package com.inar.kickercompose.ui.leaderboard

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.states.loadstates.BottomLoadOverlay
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.navigation.NavigationItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Leaderboard(vm: TestViewModel, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val users by vm.leaderboardLd.observeAsState()
    var textSearch by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        vm.loadLeaderboard()
    }

    Scaffold(topBar = {
        Card(
            modifier = Modifier.padding(7.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            shape = RoundedCornerShape(15.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                TextField(modifier = Modifier
                    .fillMaxWidth(),
                    value = textSearch,
                    onValueChange = { textSearch = it },
                    placeholder = { Text("Search") })
            }
        }
    }) {
        Box(modifier = Modifier.padding(7.dp)) {
            LazyColumn(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(users!!.value.data.filter {
                    Regex(textSearch,
                        RegexOption.IGNORE_CASE).containsMatchIn(it.name)
                }) { _, item ->
                    LeaderboardItem(user = item) {
                        navController.strangeNavigate(NavigationItems.UserPage.clearRoute + item.id)
                    }
                }
            }
        }
    }

    BottomLoadOverlay(users!!)
}




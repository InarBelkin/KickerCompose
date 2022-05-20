package com.inar.kickercompose.ui.lobby.one

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chillibits.composenumberpicker.HorizontalNumberPicker
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.lobby.item.ResultDto
import com.inar.kickercompose.data.models.states.message.messageBaseAlertWrapper
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.lobby.LobbyFuns
import com.inar.kickercompose.ui.navigation.NavigationItems
import com.inar.kickercompose.ui.navigation.showAlert
import com.inar.kickercompose.ui.theme.KickerColors
import com.inar.kickercompose.ui.theme.utils.BorderedButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MyLobbyScreens {

    @Composable
    fun Created(
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        var isStartBattleWinOpen by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        BorderedButton(text = "Start!") {
            scope.launch {
                val rez = vm.battle.checkStartBattle()
                if (!rez.success) {
                    isStartBattleWinOpen = true
                } else
                    vm.battle.startBattle()
            }
        }

        BorderedButton(text = "Stop!", color = KickerColors.dangerous, isLongPress = true) {
            scope.launch {
                LobbyFuns.StopGame(context, vm, navController)
            }
        }

        StartBattleAlert(isOpen = isStartBattleWinOpen, vm = vm) {
            isStartBattleWinOpen = false
        }

    }


    @Composable
    fun Started(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var time by remember { mutableStateOf(lobby.lastTimeStamp?.getCurrentBattleTime() ?: 0.0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(100)
                time += 0.1
            }
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = String.format("%.1f", time), fontSize = 40.sp)

            BorderedButton(text = "Pause") {
                scope.launch {
                    vm.battle.pauseBattle()
                }
            }

            BorderedButton(text = "Complete the battle") {
                scope.launch {
                    vm.battle.startEnterResultsBattle()
                }
            }

            BorderedButton(text = "Stop!", color = KickerColors.dangerous, isLongPress = true) {
                scope.launch {
                    LobbyFuns.StopGame(context, vm, navController)
                }
            }
        }
    }

    @Composable
    fun Paused(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = String.format("%.1f", lobby.lastTimeStamp?.battleTime ?: 0),
                fontSize = 40.sp)

            BorderedButton(text = "Resume") {
                scope.launch {
                    vm.battle.resumeBattle()
                }
            }


            BorderedButton(text = "Complete the battle") {
                scope.launch {
                    vm.battle.startEnterResultsBattle()
                }
            }


            BorderedButton(text = "Stop!", color = KickerColors.dangerous, isLongPress = true) {
                scope.launch {
                    LobbyFuns.StopGame(context, vm, navController)
                }
            }

        }
    }

    @Composable
    fun EnteringResults(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        var isAWinner by remember { mutableStateOf<Boolean?>(lobby.result.isWinnerA) }
        var countOfGoals by remember { mutableStateOf(lobby.result.countOfGoalsLoser) }
        Text(text = String.format("%.1f", lobby.lastTimeStamp?.battleTime ?: 0),
            fontSize = 40.sp)

        Text(text = "Who won?")
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(7.dp)
                    .clickable { isAWinner = true },
                border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(modifier = Modifier
                    .background(if (isAWinner == true) MaterialTheme.colors.primary else Color(
                        0,
                        0,
                        0,
                        0))
                    .padding(10.dp), contentAlignment = Alignment.Center) {
                    Text(text = "side A", color = MaterialTheme.colors.primaryVariant)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
                    .clickable { isAWinner = false },
                border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(modifier = Modifier
                    .background(if (isAWinner == false) MaterialTheme.colors.primary else Color(
                        0,
                        0,
                        0,
                        0))
                    .padding(10.dp), contentAlignment = Alignment.Center) {
                    Text(text = "side B", color = MaterialTheme.colors.primaryVariant)
                }
            }
        }

        Text(text = "How many goals did the loser score?")

        HorizontalNumberPicker(min = 0,
            max = 9,
            default = lobby.result.countOfGoalsLoser,
            onValueChange = { countOfGoals = it })

        BorderedButton(text = "Send result!") {
            scope.launch {
                messageBaseAlertWrapper(context) {
                    vm.battle.sendBattleResults(ResultDto().apply {
                        isWinnerA = isAWinner; countOfGoalsLoser = countOfGoals
                    })
                    val rez = vm.battle.endBattle()
                    if (!rez.success) {
                        showAlert(rez.message, context)
                    } else {
                        navController.strangeNavigate(NavigationItems.BattleResult.clearRoute + rez.battleId);
                    }
                }
            }
        }


        BorderedButton(text = "Stop!", color = KickerColors.dangerous, isLongPress = true) {
            scope.launch {
                LobbyFuns.StopGame(context, vm, navController)
            }
        }

    }

    @Composable
    fun Ended(
        lobby: LobbyItemModel,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        Text(text = "Battle is ended. Nothing to see here!")

    }
}
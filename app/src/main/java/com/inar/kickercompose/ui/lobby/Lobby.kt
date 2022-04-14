package com.inar.kickercompose.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun Lobby(model: TestViewModel) {
    val count by model.counterLiveData.observeAsState(0);



    Text(text = "lobby $count")
}
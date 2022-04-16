package com.inar.kickercompose.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.inar.kickercompose.R

@Composable
fun Lobby(model: TestViewModel) {


    Text(text = "lobby ${R.drawable.ic_baseline_account_circle_24}")
}
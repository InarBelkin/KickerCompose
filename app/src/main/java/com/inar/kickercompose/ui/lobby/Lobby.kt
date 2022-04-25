package com.inar.kickercompose.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.inar.kickercompose.R
import com.inar.kickercompose.data.viemodels.TestViewModel

@Composable
fun Lobby(model: TestViewModel) {


    Text(text = "lobby ${R.drawable.ic_baseline_account_circle_24}")
}
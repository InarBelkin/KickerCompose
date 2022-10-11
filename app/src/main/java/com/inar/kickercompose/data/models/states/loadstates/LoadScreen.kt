package com.inar.kickercompose.data.models.states.loadstates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun <T> LoadScreen(loadedContent: LoadedState<T>, SuccessContent: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (loadedContent) {
            is LoadedState.Error -> {
                Text(text = "Error:", color = Color.Red, fontSize = 18.sp)
                Text(text = loadedContent.error.message ?: "no message",
                    color = Color.Red,
                    fontSize = 18.sp)
            }
            is LoadedState.Loading -> {
                CircularProgressIndicator()
            }
            is LoadedState.Success -> SuccessContent()
        }
    }


}
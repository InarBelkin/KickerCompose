package com.inar.kickercompose.data.models.states.loadstates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> BottomLoadOverlay(loadedContent: LoadedState<T>) = when (loadedContent) {
    is LoadedState.Loading -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text(text = "loading...")
        }
    }
    is LoadedState.Error -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text(text = "Error: ${loadedContent.error.message}")
        }
    }
    else -> {}
}
package com.inar.kickercompose.ui.theme.utils

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BorderedButton(
    text: String,
    strokeColor: Color = MaterialTheme.colors.primary,
    isLongPress: Boolean = false,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        border = BorderStroke(2.dp, strokeColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = Modifier
            .combinedClickable(
                onClick = { if (!isLongPress) onClick() },
                onLongClick = { if (isLongPress) onClick() })
            .height(60.dp),
            contentAlignment = Alignment.Center) {
            Text(text = text, color = strokeColor)
        }
    }
}
package com.inar.kickercompose.data.models.states


enum class MessageStyle {
    Success, NotCorrect, Error, Nothing
}

data class MessageState(val message: String, val style: MessageStyle)
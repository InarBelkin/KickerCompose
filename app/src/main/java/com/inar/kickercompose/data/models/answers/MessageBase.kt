package com.inar.kickercompose.data.models.answers

import com.inar.kickercompose.data.models.lobby.messages.InviteMessage

class MessageBase() {
    var success: Boolean = false
    var message: String = ""

    constructor(success: Boolean, message: String) : this() {
        this.success = success
        this.message = message
    }
}
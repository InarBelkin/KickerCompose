package com.inar.kickercompose.data.models.lobby

enum class Role(val num: Int, val description: String) {
    Attack(0, "Attack"),
    Defense(1, "Defense");

    companion object {
        fun fromInt(value: Int) = Role.values().first { it.num == value }
    }
}

enum class IsAccepted(val a: Int, val description: String) {
    Empty(0, "Empty"),
    Invited(1, "Invited"),
    Accepted(2, "Accepted"),
    Refused(3, "Refused"),
    Kicked(4, "Kicked"),
    AllInvited(5, "Everyone is invited"),
    Left(6, "Left the battle");

    companion object {
        fun fromInt(value: Int) = IsAccepted.values().first { it.a == value }
    }
}

enum class UserStatus(val num: Int, val description: String) {
    Unknown(0, "Unknown"),
    Offline(1, "Offline"),
    Online(2, "Online"),
    InBattle(3, "In Battle");

    companion object {
        fun fromInt(value: Int) = UserStatus.values().first { it.num == value }
    }


}

enum class BattleStatus(val num: Int, val description: String) {
    Created(0, "Created"),
    Started(1, "Started"),
    Paused(2, "Paused"),
    EnteringResults(3, "Entering Results"),
    Ended(4, "Ended");

    companion object {
        fun fromInt(value: Int) = values().first { it.num == value }
    }
}
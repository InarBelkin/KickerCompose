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
    Refused(3, "Invited"),
    Kicked(4, "Kicked"),
    AllInvited(5, "Everyone is invited");

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
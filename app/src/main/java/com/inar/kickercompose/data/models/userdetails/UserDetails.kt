package com.inar.kickercompose.data.models.userdetails

import com.inar.kickercompose.data.models.UserLeaderboard

class UserDetails {
    var id: String = ""
    var name: String = "";
    var imgSource: String = ""

    var statsOneVsOne = StatsOneVsOne()
    var statsTwoVsTwo = StatsTwoVsTwo()

    fun toUserLeaderboard() = UserLeaderboard().apply {
        id = this@UserDetails.id
        name = this@UserDetails.name
        elo = statsOneVsOne.elo
        countOfBattles =
            statsOneVsOne.battlesCount + statsTwoVsTwo.battlesCountInAttack + statsTwoVsTwo.battlesCountInDefense
        winsCount =
            statsOneVsOne.winsCount + statsTwoVsTwo.winsCountInAttack + statsTwoVsTwo.winsCountInDefense
    }

}
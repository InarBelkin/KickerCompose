package com.inar.kickercompose.data.models.userdetails

import com.inar.kickercompose.data.models.UserLeaderboard

class UserDetails {
    var id: String = ""
    var name: String = "";
    var imgSource: String = ""
    var isMe = false

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

    val statsOneVsOneList by lazy {
        statsOneVsOne.let {
            listOf(
                "Battles count 1x1" to it.battlesCount.toString(),
                "Percent of wins" to if (it.battlesCount > 10)
                    it.winsCount.toDouble().div(it.battlesCount).toInt().toString() + "%"
                else "not enough battles",
                "ELO" to it.elo.toString(),
                "Wins count" to it.winsCount.toString(),
                "Defeats count" to it.battlesCount.minus(it.winsCount).toString(),
                "Count of goals" to ""
            )
        }
    }

    val statsTwoVsTwoList: List<Pair<String, String>> by lazy {
        statsTwoVsTwo.let {
            listOf("Battles count 2x2" to it.battlesCountInAttack.plus(it.battlesCountInDefense)
                .toString(),
                "Elo" to it.elo.toString())
        }
    }


}
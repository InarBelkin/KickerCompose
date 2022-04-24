package com.inar.kickercompose.ui

import com.inar.kickercompose.R

sealed class NavigationItems(
    var route: String,
    var icon: Int,
    var title: String
) {
    object Leaderboard :
        NavigationItems("leaderboard", R.drawable.ic_baseline_people_24, "Leaderboard")

    object Lobby :
        NavigationItems("lobby", R.drawable.ic_baseline_people_24, "Lobby")

    object MyPage :
        NavigationItems("my_page", R.drawable.ic_baseline_people_24, "Me")

    object UserPage :
        NavigationItems(
            "user_page/{userId}",
            R.drawable.ic_baseline_person_24,
            "UserPage",
        ) {
        const val userId = "userId"
        const val clearRoute = "user_page/"
    }
}

sealed class AccountItems(
    val route: String
){
    object Login: AccountItems("login")
    object Registration: AccountItems("registration")
}

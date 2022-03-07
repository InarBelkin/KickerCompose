package com.inar.kickercompose.ui

import com.inar.kickercompose.R

sealed class NavigationItems(var route: String, var icon: Int, var title: String) {
    object Leaderboard :
        NavigationItems("leaderboard", R.drawable.ic_baseline_people_24, "Leaderboard");
    object Lobby :
        NavigationItems("lobby", R.drawable.ic_baseline_people_24, "Lobby");
    object MyPage :
        NavigationItems("my_page", R.drawable.ic_baseline_people_24, "Me");


}

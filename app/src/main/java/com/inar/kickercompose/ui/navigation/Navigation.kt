package com.inar.kickercompose.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.ui.Lobby
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.leaderboard.Leaderboard
import com.inar.kickercompose.ui.mypage.MyPage
import com.inar.kickercompose.ui.userpage.UserPage

@Composable
fun Navigation(navHostController: NavHostController, vm:TestViewModel) {


    NavHost(navController = navHostController, NavigationItems.Leaderboard.route) {

        composable(NavigationItems.Leaderboard.route) {
            Leaderboard(vm, navHostController);
        }

        composable(NavigationItems.Lobby.route) {
            Lobby(vm)
        }
        composable(NavigationItems.MyPage.route) {
            MyPage(vm)
        }

        composable(
            NavigationItems.UserPage.route,
            arguments = listOf(navArgument(NavigationItems.UserPage.userId) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            UserPage(
                vm = vm,
                userId = backStackEntry.arguments?.getString(NavigationItems.UserPage.userId) ?: ""
            )
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavigationItems.Leaderboard,
        NavigationItems.Lobby,
        NavigationItems.MyPage
    )

    BottomNavigation() {
        val navBackStackEntry by navController.currentBackStackEntryAsState();
        val currentRoute = navBackStackEntry?.destination?.route;

        items.forEach { item ->
            BottomNavigationItem(icon = {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title
                )
            },
                label = {
                    Text(text = item.title)
                },
                selected = currentRoute == item.route, onClick = {
                    navController.strangeNavigate(item.route)
                })
        }

    }

}
package com.inar.kickercompose.ui.navigation

import android.app.Activity
import android.media.audiofx.Equalizer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationManagerCompat
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
import com.inar.kickercompose.services.ServiceUtil
import com.inar.kickercompose.ui.leaderboard.Leaderboard
import com.inar.kickercompose.ui.lobby.InviteInLobby
import com.inar.kickercompose.ui.lobby.LobbyFuns
import com.inar.kickercompose.ui.lobby.MyLobby
import com.inar.kickercompose.ui.userpage.MyPage
import com.inar.kickercompose.ui.userpage.SettingsPage
import com.inar.kickercompose.ui.userpage.UserPage

@Composable
fun Navigation(navHostController: NavHostController, vm: TestViewModel) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        LobbyFuns.acceptInvite(context, navHostController, vm)
    }

    NavHost(navController = navHostController, NavigationItems.Leaderboard.route) {

        composable(NavigationItems.Leaderboard.route) {
            Leaderboard(vm, navHostController);
        }

        composable(NavigationItems.Lobby.route) {
            Lobby(vm, navHostController)
        }
        composable(NavigationItems.MyPage.route) {
            MyPage(vm, navHostController)
        }

        composable(NavigationItems.MyLobby.route) {
            MyLobby(vm, navHostController)
        }

        composable(NavigationItems.InviteInMyLobby.route,
            arguments = listOf(
                navArgument(NavigationItems.InviteInMyLobby.side) {
                    type = NavType.IntType
                }, navArgument(NavigationItems.InviteInMyLobby.position) {
                    type = NavType.IntType
                })) {
            InviteInLobby(
                vm = vm, navController = navHostController,
                side = it.arguments?.getInt(NavigationItems.InviteInMyLobby.side)!!,
                position = it.arguments?.getInt(NavigationItems.InviteInMyLobby.position)!!,
            )

        }

        composable(NavigationItems.SettingsPage.route) {
            SettingsPage(vm = vm, navController = navHostController)
        }

        composable(
            NavigationItems.UserPage.route,
            arguments = listOf(navArgument(NavigationItems.UserPage.userId) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            UserPage(
                vm = vm,
                userId = backStackEntry.arguments?.getString(NavigationItems.UserPage.userId) ?: "",
                navHostController = navHostController
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
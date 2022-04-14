package com.inar.kickercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inar.kickercompose.ui.*
import com.inar.kickercompose.ui.mypage.MyPage
import com.inar.kickercompose.ui.theme.KickerComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KickerComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen("Android")
                }
            }
        }
    }
}


@Composable
fun MainScreen(name: String) {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavBar(navController) }) {
        Box(modifier = Modifier.padding(it)) {
            Navigation(navHostController = navController)
        }
    }
}

@Composable
fun Navigation(navHostController: NavHostController) {
    val vm: TestViewModel = viewModel();
    NavHost(navController = navHostController, NavigationItems.Leaderboard.route) {
        composable(NavigationItems.Leaderboard.route) {
            Leaderboard();
        }
        composable(NavigationItems.Lobby.route) {
            Lobby(vm)
        }
        composable(NavigationItems.MyPage.route) {
            MyPage(vm)
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
        var currentRoute = navBackStackEntry?.destination?.route;

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
                    navController.navigate(item.route) {
                        popUpTo(item.route) {
                            inclusive = true;
                        }
                        launchSingleTop = true;
                    }
                })
        }

    }

}

@Composable
fun Test() {
    Canvas(modifier = Modifier.fillMaxSize()) {
    }
}

@Composable
fun BottomNavigationBarWithButtons(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(onClick = {
            navController.navigate(NavigationItems.Leaderboard.route) {
                popUpTo(NavigationItems.Leaderboard.route) {
                    inclusive = true;
                }
                launchSingleTop = true;

            }
        }) {
            Text(text = "Leaderboard")
        }
        Button(onClick = {
            navController.navigate(NavigationItems.Lobby.route) {
                popUpTo(NavigationItems.Lobby.route) {
                    inclusive = true;
                }
                launchSingleTop = true;

            }
        }) {
            Text(text = "Lobby")
        }
        Button(onClick = {
            navController.navigate(NavigationItems.MyPage.route) {
                popUpTo(NavigationItems.MyPage.route) {
                    inclusive = true;
                }
                launchSingleTop = true;

            }
        }) {
            Text(text = "Me")
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KickerComposeTheme {
        MainScreen("Android")
    }
}
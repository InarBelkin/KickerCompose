package com.inar.kickercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.inar.kickercompose.data.models.states.auth.AuthState
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.ui.account.AccountPage
import com.inar.kickercompose.ui.navigation.BottomNavBar
import com.inar.kickercompose.ui.navigation.Navigation
import com.inar.kickercompose.ui.theme.KickerComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    val context = LocalContext.current
    val vm: TestViewModel = hiltViewModel();
    val authState by vm.account.authState.observeAsState()

    LaunchedEffect(Unit) {
        vm.account.loginRefresh()
    }

    when (authState) {
        is AuthState.Loading, is AuthState.Logged -> StandardApp(vm)
        is AuthState.Error -> Text(text = "Login error")    //TODO:change connection error page and add button "try again"
        is AuthState.NotLogged -> AccountPage(vm)
    }

}

@Composable
fun StandardApp(vm:TestViewModel) {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavBar(navController) }) {
        Box(modifier = Modifier.padding(it)) {
            Navigation(navHostController = navController, vm)
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
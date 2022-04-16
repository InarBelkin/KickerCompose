package com.inar.kickercompose.ui.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.inar.kickercompose.ui.TestViewModel

@Composable
fun MyPage(model: TestViewModel) {

    val user by model.userDetailsLiveData.observeAsState();

    LaunchedEffect(Unit) {
        model.loadUserDetails("27e41e0b-4de9-4de8-bb99-6cf41488fc05")
    }


    val counter2 = remember {
        mutableStateOf(0)
    }
    Column() {

        Text(text = "user=  ${user?.name}")

    }


}


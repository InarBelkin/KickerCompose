package com.inar.kickercompose.ui.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.inar.kickercompose.ui.TestViewModel

@Composable
fun MyPage(model: TestViewModel) {

    val count by model.counterLiveData.observeAsState(0);
    val user by model.UserDetailLiveData.observeAsState();
    model.loadUserDetails("27e41e0b-4de9-4de8-bb99-6cf41488fc06")

    val counter2 = remember {
        mutableStateOf(0)
    }
    Column() {
        Text(text = "vmCat = ${count}");
        Text(text = "vmCat = ${model.counterLiveData.value}");
        Button(onClick = { model.increaseCounter() }) {


        }

        Text(text = "user=  ${user?.name}")

    }


}


package com.inar.kickercompose.ui.lobby

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.navigation.NavHostController
import com.inar.kickercompose.data.models.lobby.IsAccepted
import com.inar.kickercompose.data.models.lobby.messages.InviteMessage
import com.inar.kickercompose.data.viemodels.TestViewModel
import com.inar.kickercompose.other.strangeNavigate
import com.inar.kickercompose.services.ServiceUtil
import com.inar.kickercompose.ui.navigation.NavigationItems
import com.inar.kickercompose.ui.navigation.showAlert
import java.lang.Exception

const val LOBBY_TAG = "lobby"

suspend fun createLobbyButton(
    vm: TestViewModel,
    navController: NavHostController,
    context: Context,
) {
    try {
        val message = vm.battle.startBattle()
        if (message.success)
            navController.strangeNavigate(NavigationItems.MyLobby.route)
        else showAlert("message", context)
    } catch (e: Exception) {
        Log.e(LOBBY_TAG, e.message ?: "")
        showAlert(e.localizedMessage ?: "something went wrong", context)
    }
}

suspend fun toMyBattle(vm: TestViewModel, navController: NavHostController) {
    navController.strangeNavigate(NavigationItems.MyLobby.route)
}


fun openInviteWin(navController: NavHostController, side: Int, position: Int) {
    navController.strangeNavigate("${NavigationItems.InviteInMyLobby.clearRoute}$side/$position")
}

suspend fun addMe(
    context: Context,
    vm: TestViewModel,
    navController: NavHostController,
    side: Int,
    position: Int,
) {
    try {
        val lobbyItem = vm.battle.myLobbyLd.value!!.value!!;
        val item = if (side == 0) lobbyItem.sideA[position]
        else lobbyItem.sideB[position]

        item.id = vm.account.getUserClaims()!!.id
        item.role = position
        item.accepted = IsAccepted.Accepted.a

        vm.battle.updateBattle(lobbyItem)
        navController.strangeNavigate(NavigationItems.MyLobby.route)
    } catch (e: Exception) {
        Log.e(LOBBY_TAG, e.message ?: "")
        showAlert(e.localizedMessage ?: "something went wrong", context)
    }
}

object LobbyFuns {
    suspend fun inviteOne(
        context: Context,
        vm: TestViewModel,
        navController: NavHostController,
        invitedId: String,
        side: Int, position: Int,
    ) {
        try {
            val message = InviteMessage().also {
                it.senderId = vm.account.getUserClaims()?.id ?: ""
                it.invitedId = invitedId
                it.isInviteToAll = false
                it.message = vm.battle.myLobbyLd.value?.value?.message ?: ""
                it.position = position
                it.side = side
                it.senderName = vm.account.getUserClaims()?.name ?: ""
            }
            vm.battle.inviteOne(invitedId, message)
            navController.strangeNavigate(NavigationItems.MyLobby.route)
        } catch (e: Exception) {
            Log.e(LOBBY_TAG, e.message ?: "")
            showAlert(e.localizedMessage ?: "something went wrong", context)
        }


    }

    suspend fun StopGame(
        context: Context,
        vm: TestViewModel,
        navController: NavHostController,
    ) {
        try {
            vm.battle.stopBattle(vm.account.getUserClaims()!!.id)
            navController.strangeNavigate(NavigationItems.Lobby.route)
        } catch (e: Exception) {
            Log.e(LOBBY_TAG, e.message ?: "")
            showAlert(e.localizedMessage ?: "something went wrong", context)

        }

    }

    suspend fun acceptInvite(
        context: Context,
        navController: NavHostController,
        vm: TestViewModel,
    ) {
        if (context is Activity) {
            try {
                val isAccept = context.intent.getBooleanExtra(ServiceUtil.OPEN_LOBBY_EXTRA, false)
                if (isAccept) {

                    val message =
                        context.intent.getParcelableExtra<InviteMessage>(ServiceUtil.INVITE_MESSAGE_EXTRA)

                    vm.battle.sendInviteAnswer(message!!, true)

                    navController.strangeNavigate(NavigationItems.Lobby.route)
                }
            } catch (e: Exception) {
                Log.e(LOBBY_TAG, e.message ?: "")
                showAlert(e.localizedMessage ?: "something went wrong", context)
            }
        }

    }
}


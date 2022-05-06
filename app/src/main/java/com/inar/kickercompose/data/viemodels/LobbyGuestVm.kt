package com.inar.kickercompose.data.viemodels

import androidx.lifecycle.ViewModel
import com.inar.kickercompose.data.models.lobby.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.net.repositories.ILobbyMessagesRepository
import com.inar.kickercompose.data.net.repositories.ILobbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LobbyGuestVm @Inject constructor(
    private val lobby: ILobbyRepository,
    private val lobbyMessages: ILobbyMessagesRepository,
) : ViewModel() {
    private val delegateGuestLobby = LoadedState.DelegateLiveData<LobbyItemModel>(LobbyItemModel())
    val guestLobbyLd by delegateGuestLobby

    suspend fun reloadLobby(initiatorId: String) {
        delegateGuestLobby.reLoad { lobby.getLobby(initiatorId) }
    }

}
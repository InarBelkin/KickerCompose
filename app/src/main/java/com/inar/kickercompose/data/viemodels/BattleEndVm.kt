package com.inar.kickercompose.data.viemodels

import androidx.lifecycle.ViewModel
import com.inar.kickercompose.data.models.lobby.item.LobbyItemModel
import com.inar.kickercompose.data.models.states.loadstates.LoadedState
import com.inar.kickercompose.data.net.repositories.interfaces.IBattleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BattleEndVm @Inject constructor(private val battle: IBattleRepository) : ViewModel() {

    private val delegateBattle = LoadedState.DelegateLiveData(LobbyItemModel())
    val battleResultLd by delegateBattle;

    suspend fun reloadBattleResult(id: String) {
        delegateBattle.reLoad { battle.getBattle(id) }
    }

}
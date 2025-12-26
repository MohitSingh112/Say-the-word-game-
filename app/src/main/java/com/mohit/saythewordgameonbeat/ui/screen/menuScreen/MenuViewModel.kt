package com.mohit.saythewordgameonbeat.ui.screen.menuScreen

import androidx.lifecycle.ViewModel
import com.mohit.saythewordgameonbeat.emuns.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(): ViewModel() {

    private val _currentGameMode = MutableStateFlow(GameMode.SinglePlayer)
    val currentGameMode = _currentGameMode.asStateFlow()

    fun onGameModeChanged(gameMode: GameMode){
        _currentGameMode.value = gameMode
    }
}
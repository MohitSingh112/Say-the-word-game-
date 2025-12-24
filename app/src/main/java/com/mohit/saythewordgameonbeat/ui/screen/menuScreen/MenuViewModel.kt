package com.mohit.saythewordgameonbeat.ui.screen.menuScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.saythewordgameonbeat.data.GamePreferencesRepository
import com.mohit.saythewordgameonbeat.emuns.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: GamePreferencesRepository
) : ViewModel() {

    val currentGameMode: StateFlow<GameMode> = repository.getGameMode()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameMode.SinglePlayer
        )

    fun onGameModeChanged(newMode: GameMode) {
        viewModelScope.launch {
            repository.saveGameMode(newMode)
        }
    }
}
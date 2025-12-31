package com.mohit.saythewordgameonbeat.ui.screen.tapToBeatScreen

import androidx.lifecycle.ViewModel
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.GameMode
import com.mohit.saythewordgameonbeat.engine.GameEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TapToBeatViewModel @Inject constructor(
    private val gameEngine: GameEngine
) : ViewModel() {

    // --- EXPOSE ENGINE STATE DIRECTLY ---
    val score = gameEngine.score
    val lives = gameEngine.lives
    val highlightedIndex = gameEngine.highlightedIndex
    val isMemorizationPhase = gameEngine.isMemorizationPhase
    val gridBlock = gameEngine.gridBlock
    val userOptions = gameEngine.userOptions
    val currentPlayer = gameEngine.currentPlayer
    val isPaused = gameEngine.isPaused
    val gameOver = gameEngine.gameOver
    val showNextPlayerDialog = gameEngine.showNextPlayerDialog
    val gameResult = gameEngine.gameResult
    val vibrationEffect = gameEngine.vibrationEffect

    // --- USER ACTIONS ---

    fun startGame(difficulty: Difficulty, gameMode: GameMode) {
        gameEngine.startGame(difficulty, gameMode)
    }

    fun onOptionClicked(selectedWord: String) {
        gameEngine.onOptionClicked(selectedWord)
    }

    fun pauseGame() {
        gameEngine.pauseGame()
    }

    fun resumeGame() {
        gameEngine.resumeGame()
    }

    fun startNextPlayerTurn() {
        gameEngine.startNextPlayerTurn()
    }

    fun retry() {
        gameEngine.retry()
    }

    // --- CLEANUP ---
    override fun onCleared() {
        super.onCleared()
        gameEngine.cleanup()
    }
}
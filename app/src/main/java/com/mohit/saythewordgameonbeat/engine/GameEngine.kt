package com.mohit.saythewordgameonbeat.engine

import com.mohit.saythewordgameonbeat.BeatPlayer
import com.mohit.saythewordgameonbeat.Repository.BlockRepository
import com.mohit.saythewordgameonbeat.data.Block
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.GameMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GameEngine @Inject constructor(
    private val blockRepository: BlockRepository,
    private val beatPlayer: BeatPlayer

) {
    // --- STATE FLOWS (Read-Only for ViewModel) ---
    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _lives = MutableStateFlow(3)
    val lives = _lives.asStateFlow()

    private val _highlightedIndex = MutableStateFlow(-1)
    val highlightedIndex = _highlightedIndex.asStateFlow()

    private val _isMemorizationPhase = MutableStateFlow(false)
    val isMemorizationPhase = _isMemorizationPhase.asStateFlow()

    private val _gridBlock = MutableStateFlow<List<Block>>(emptyList())
    val gridBlock = _gridBlock.asStateFlow()

    private val _userOptions = MutableStateFlow<List<Block>>(emptyList())
    val userOptions = _userOptions.asStateFlow()

    private val _currentPlayer = MutableStateFlow(1)
    val currentPlayer = _currentPlayer.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver = _gameOver.asStateFlow()

    private val _showNextPlayerDialog = MutableStateFlow(false)
    val showNextPlayerDialog = _showNextPlayerDialog.asStateFlow()

    private val _gameResult = MutableStateFlow("")
    val gameResult = _gameResult.asStateFlow()

    // Events (Vibration, etc.)
    private val _vibrationEffect = MutableSharedFlow<Unit>()
    val vibrationEffect = _vibrationEffect.asSharedFlow()

    // --- INTERNAL STATE ---
    private var engineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var gameJob: Job? = null

    private var currentGameMode = GameMode.SinglePlayer
    private var currentDifficulty = Difficulty.Easy
    private var currentLevel = 1

    private var gameSpeed = 400L

    // Logic Flags
    private var isCurrentBeatSolved = false
    private var player1FinalScore = 0
    private var player2FinalScore = 0

    // --- PUBLIC METHODS ---

    fun startGame(difficulty: Difficulty, gameMode: GameMode) {
        // Reset Everything
        currentDifficulty = difficulty
        currentGameMode = gameMode
        player1FinalScore = 0
        player2FinalScore = 0
        _currentPlayer.value = 1
        _gameResult.value = ""

        startRound()
    }

    fun startNextPlayerTurn() {
        startRound()
    }

    fun pauseGame() {
        if (!_gameOver.value && !_showNextPlayerDialog.value) {
            _isPaused.value = true
            beatPlayer.pauseBackgroundMusic()
        }
    }

    fun resumeGame() {
        if (!_gameOver.value) {
            _isPaused.value = false
            beatPlayer.resumeBackgroundMusic()
        }
    }

    fun retry() {
        startGame(currentDifficulty, currentGameMode)
    }

    fun cleanup() {
        gameJob?.cancel()
        beatPlayer.stopAll()
    }

    fun onOptionClicked(selectedWord: String) {
        // Block input if game is over, paused, or memorizing
        if (_gameOver.value || _showNextPlayerDialog.value || _isPaused.value || _isMemorizationPhase.value) return

        // Prevent double tapping
        if (isCurrentBeatSolved) return

        val currentIndex = _highlightedIndex.value
        if (currentIndex == -1) return // Game hasn't highlighted anything yet

        val correctWord = _gridBlock.value.getOrNull(currentIndex)?.text

        if (selectedWord == correctWord) {
            _score.value += 1
            isCurrentBeatSolved = true
        } else {
            handleMistake()
            isCurrentBeatSolved = true // Mark solved so they don't get hit by timeout too
        }
    }

    // --- INTERNAL LOOPS ---

    private fun startRound() {
        // Reset Level Stats
        _lives.value = when (currentDifficulty) {
            Difficulty.NightMare -> 1
            Difficulty.Hard -> 2
            else -> 900// todo
        }
        _score.value = 0
        currentLevel = 1

        _gameOver.value = false
        _showNextPlayerDialog.value = false

        loadNewLevel()

        // Start with Memorization
        runMemorizationPhase()
    }

    private fun runMemorizationPhase() {
        gameJob?.cancel()

            gameJob = engineScope.launch {
                beatPlayer.playMemorizationMusic()

                _isMemorizationPhase.value = true
                _highlightedIndex.value = -1

                // Loop 0 to 7
                for (i in 0..7) {
                    // Pause Check
                    while (_isPaused.value) {
                        delay(100)
                    }
                    if (_gameOver.value) break

                    _highlightedIndex.value = i
                    delay(gameSpeed) // memorization time
                }

                _highlightedIndex.value = -1
                _isMemorizationPhase.value = false

                // Start the REAL game
                runGameLoop()
            }

    }

    private fun runGameLoop() {
        gameJob?.cancel()

            gameJob = engineScope.launch {

                // Don't play music or set up the grid until the user clicks Resume.
                while (_isPaused.value) {
                    delay(100)
                }

                // Start Background Beat
                beatPlayer.playGameBackgroundMusic()

                _highlightedIndex.value = -1
                isCurrentBeatSolved = true

                while (!_gameOver.value && !_showNextPlayerDialog.value) {

                    // --- TIMEOUT CHECK ---
                    if (_highlightedIndex.value != -1 && !isCurrentBeatSolved) {
                        handleMistake()
                        if (_gameOver.value || _showNextPlayerDialog.value) break
                    }

                    // --- PAUSE CHECK ---
                    while (_isPaused.value) {
                        delay(100)
                    }

                    // --- MOVE HIGHLIGHTER ---
                    val nextIndex = (_highlightedIndex.value + 1)
                    if (nextIndex >= 8) {
                        handleLevelComplete()
                        _highlightedIndex.value = -1
                        // If you don't return here, the code below runs and messes up the new level state.
                        return@launch
                    } else {
                        _highlightedIndex.value = nextIndex
                    }

                    // Reset Input Flag
                    isCurrentBeatSolved = false

                    // --- WAIT FOR NEXT BEAT ---
                    delay(gameSpeed)
                }

        }
    }

    private fun loadNewLevel() {
        val baseSet = blockRepository.getBlocks(currentDifficulty)

        // Create 8 blocks for grid (allow duplicates)
        val newGrid = ArrayList<Block>()
        while (newGrid.size < 8) {
            newGrid.add(baseSet.random())
        }
        _gridBlock.value = newGrid

        // Create Options
        val optionCount = when (currentDifficulty) {
            Difficulty.Easy -> 2
            Difficulty.Medium -> 3
            Difficulty.Hard -> 4
            Difficulty.NightMare -> 5
            else -> 2
        }
        val correctOptions = baseSet.toMutableList()
        while (correctOptions.size < optionCount) {
            correctOptions.add(baseSet.random())
        }
        _userOptions.value = correctOptions.shuffled().take(optionCount)
    }

    private fun handleLevelComplete() {
        currentLevel++

        loadNewLevel()

        // RESTART MEMORIZATION (New pattern)
        runMemorizationPhase()
    }

    private fun handleMistake() {
        engineScope.launch { _vibrationEffect.emit(Unit) }

        val newLives = _lives.value - 1
        _lives.value = newLives

        if (newLives <= 0) {
            handleRoundOver()
        }
    }

    private fun handleRoundOver() {
        gameJob?.cancel()
        beatPlayer.stopAll()

        if (currentGameMode == GameMode.SinglePlayer) {
            player1FinalScore = _score.value
            _gameResult.value = "Score: $player1FinalScore"
            _gameOver.value = true
        } else {
            // Two Player Logic
            if (_currentPlayer.value == 1) {
                player1FinalScore = _score.value
                _currentPlayer.value = 2
                _showNextPlayerDialog.value = true
            } else {
                player2FinalScore = _score.value
                determineWinner()
                _gameOver.value = true
            }
        }
    }

    private fun determineWinner() {
        val p1 = player1FinalScore
        val p2 = player2FinalScore
        _gameResult.value = when {
            p1 > p2 -> "Player 1 Wins!\n\nP1: $p1  |  P2: $p2"
            p2 > p1 -> "Player 2 Wins!\n\nP1: $p1  |  P2: $p2"
            else -> "It's a Draw!\n\nP1: $p1  |  P2: $p2"
        }
    }
}
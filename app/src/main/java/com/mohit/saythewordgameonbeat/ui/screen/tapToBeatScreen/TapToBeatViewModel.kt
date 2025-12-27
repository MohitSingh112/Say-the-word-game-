package com.mohit.saythewordgameonbeat.ui.screen.tapToBeatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.saythewordgameonbeat.Repository.BlockRepository
import com.mohit.saythewordgameonbeat.data.Block
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class TapToBeatViewModel @Inject constructor(
    private val blockRepository : BlockRepository,

) : ViewModel() {

    // --- STATE VARIABLES (Observed by UI) ---

    // Game Stats
    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _lives = MutableStateFlow(3)
    val lives = _lives.asStateFlow()

    // Grid State
    private val _highlightedIndex = MutableStateFlow(-1) // 0-7, or -1 if waiting
    val highlightedIndex = _highlightedIndex.asStateFlow()

    private val _isMemorizationPhase = MutableStateFlow(false)
    val isMemorizationPhase = _isMemorizationPhase.asStateFlow()

    private val _gridBlock = MutableStateFlow<List<Block>>(emptyList())
    val gridBlock = _gridBlock.asStateFlow()

    private val _userOptions = MutableStateFlow<List<Block>>(emptyList())
    val userOptions = _userOptions.asStateFlow()

    // 2-Player State
    private val _currentPlayer = MutableStateFlow(1) // 1 or 2
    val currentPlayer = _currentPlayer.asStateFlow()

    //Paused State
    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    // Dialog Control
    private val _showNextPlayerDialog = MutableStateFlow(false) // "Pass phone to P2"
    val showNextPlayerDialog = _showNextPlayerDialog.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver = _gameOver.asStateFlow()

    private val _gameResult = MutableStateFlow("") // "Player 1 Wins!" etc.
    val gameResult = _gameResult.asStateFlow()

    // Side Effects
    private val _vibrationEffect = MutableSharedFlow<Unit>()
    val vibrationEffect: SharedFlow<Unit> = _vibrationEffect

    // --- INTERNAL ENGINE VARIABLES ---
    private var gameJob: Job? = null
    private var currentGameMode = GameMode.SinglePlayer

    private var currentDifficulty = Difficulty.Easy

    private var currentLevel = 1
    private var baseBeatInterval = 1000L // Starts at 1 second per beat
    private var currentSpeedMultiplier = 1.0f

    // Logic Flags
    private var isCurrentBeatSolved = false // Tracks if user tapped for the current block
    private var player1FinalScore = 0
    private var player2FinalScore = 0


    // --- FUNCTIONS ---

    fun startGame(difficulty: Difficulty,gameMode: GameMode) {
        currentDifficulty = difficulty
        currentGameMode = gameMode

        // Reset Global Game State
        player1FinalScore = 0
        player2FinalScore = 0
        _currentPlayer.value = 1
        _gameResult.value = ""

        // Start Player 1's Turn
        startTurn()
    }

    // Starts a fresh round for whoever is the current player
    private fun startTurn() {
        // Reset Round Stats
        _lives.value = when (currentDifficulty) {
            Difficulty.NightMare -> 1
            Difficulty.Hard -> 2
            else -> 3
        }
        _score.value = 0
        currentLevel = 1
        currentSpeedMultiplier = 1.0f

        // UI Cleanup
        _gameOver.value = false
        _showNextPlayerDialog.value = false

        loadNewLevel()
        startMemorizationSequence()
    }

    // The Main Game Loop (Metronome)
    private fun startBeatLoop() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            _highlightedIndex.value = -1
            isCurrentBeatSolved = true // Don't penalize the warm-up period

            delay(1000) // 1 Second Warmup before beat starts


            while (!_gameOver.value && !_showNextPlayerDialog.value) {

                // 1. TIMEOUT CHECK: Did user miss the previous beat?
                // If the highlighter was active on a block (-1 is inactive), and they didn't solve it...
                if (_highlightedIndex.value != -1 && !isCurrentBeatSolved) {
                    handleMistake() // Lose a life
                    // If they died from that mistake, stop the loop immediately
                    if (_gameOver.value || _showNextPlayerDialog.value) break
                }

                // If paused, we just spin here and wait until resumed
                while (_isPaused.value) {
                    delay(100) // Check every 100ms
                }

                // 2. MOVE HIGHLIGHTER
                val nextIndex = (_highlightedIndex.value + 1)

                if (nextIndex >= 8) {
                    // Completed a full loop of 8 blocks
                    handleLevelComplete()
                    _highlightedIndex.value = 0
                } else {
                    _highlightedIndex.value = nextIndex
                }

                // 3. RESET FLAG: New block, not solved yet
                isCurrentBeatSolved = false

                // 4. WAIT: Calculate speed
                val delayTime = (baseBeatInterval / currentSpeedMultiplier).toLong()
                delay(delayTime)
            }
        }
    }

    fun pauseGame() {
        if (!_gameOver.value) {
            _isPaused.value = true
        }
    }

    fun resumeGame() {
        _isPaused.value = false
    }

    private fun loadNewLevel() {
        //get the block list
        val baseSet: List<Block> = blockRepository.getBlocks(currentDifficulty)

        //put the blocks in random order
        val newGrid: ArrayList<Block> = ArrayList()
        while (newGrid.size < 8) {
            newGrid.add(baseSet.random())
        }
        _gridBlock.value = newGrid.take(8)

        // generate user options (Buttons)
        val optionCount = when (currentDifficulty) {
            Difficulty.Easy -> 4
            Difficulty.Medium -> 6
            Difficulty.Hard -> 8
            Difficulty.NightMare -> 10
            else -> 4
        }

        val correctOptions: MutableList<Block> = baseSet.toMutableList()

        // if the base set of blocks are not equal to the option count,and duplicate blocks from base
        while (correctOptions.size < optionCount) {
            correctOptions.add(baseSet.random())
        }
        _userOptions.value = correctOptions
            .shuffled()
            .take(optionCount)//todo(think about the shuffle nightmare and hard only or for all )
    }

    private fun handleLevelComplete() {
        currentLevel++
        // Speed up after level 3, capping at 3x speed
        if (currentLevel > 3) {
            currentSpeedMultiplier = min(3.0f, currentSpeedMultiplier + 0.2f)
        }
        loadNewLevel()
        startMemorizationSequence()
    }

    fun onOptionClicked(selectedWord: String) {
        if (_gameOver.value || _showNextPlayerDialog.value || _isPaused.value || _isMemorizationPhase.value) return

        // Prevent farming points on the same block
        if (isCurrentBeatSolved) return

        val currentIndex = _highlightedIndex.value
        // Ignore clicks if game hasn't started moving yet
        if (currentIndex == -1) return

        val correctWord = _gridBlock.value.getOrNull(currentIndex)?.text

        if (selectedWord == correctWord) {
            // Correct Tap
            _score.value += 1
            isCurrentBeatSolved = true
        } else {
            // Wrong Tap
            handleMistake()
            // Mark as solved so they don't get double penalized (once for click, once for timeout)
            isCurrentBeatSolved = true
        }
    }

    private fun handleMistake() {
        // Trigger Vibration
        viewModelScope.launch { _vibrationEffect.emit(Unit) }

        val newLives = _lives.value - 1
        _lives.value = newLives

        if (newLives <= 0) {
            handleRoundOver()
        }
    }

    private fun handleRoundOver() {
        gameJob?.cancel()

        if (currentGameMode == GameMode.SinglePlayer) {
            // SINGLE PLAYER: Game ends immediately
            player1FinalScore = _score.value
            _gameResult.value = "Score: $player1FinalScore"
            _gameOver.value = true
        } else {
            // TWO PLAYER
            if (_currentPlayer.value == 1) {
                // Player 1 finished. Save score, prompt Player 2.
                player1FinalScore = _score.value
                _currentPlayer.value = 2
                _showNextPlayerDialog.value = true
            } else {
                // Player 2 finished. Game Over.
                player2FinalScore = _score.value
                determineWinner()
                _gameOver.value = true
            }
        }
    }

    private fun startMemorizationSequence() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            _isMemorizationPhase.value = true
            _highlightedIndex.value = -1

            delay(1000) // Initial wait

            // Loop through all 8 blocks automatically
            for (i in 0..7) {
                // Check for pause/game over inside the loop
                while (_isPaused.value) { delay(100) }
                if (_gameOver.value) break

                _highlightedIndex.value = i
                delay(600) // Speed of memorization (0.6 seconds per block)
            }

            _highlightedIndex.value = -1
            _isMemorizationPhase.value = false

            delay(500) // Small pause before real game starts
            startBeatLoop() // <--- STARTS THE GAME LOOP AUTOMATICALLY
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

    // Called when the "Start Turn" button is clicked in the dialog
    fun startNextPlayerTurn() {
        startTurn()
    }

    // Called when clicking "Restart" on Game Over
    fun retry() {
        startGame(currentDifficulty, currentGameMode)
    }
}
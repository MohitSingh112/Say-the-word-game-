package com.mohit.saythewordgameonbeat.ui.screen.tapToBeatScreen

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.data.Block
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.GameMode

// --------------------------------------------------------------------------
// 1. STATEFUL SCREEN (Handles ViewModel, Logic, and Effects)
// --------------------------------------------------------------------------
@Composable
fun TapToBeatGameScreen(
    difficulty: Difficulty,
    gameMode: GameMode,
    onBack: () -> Unit,
    viewModel: TapToBeatViewModel = hiltViewModel()
) {
    // Collect State
    val score by viewModel.score.collectAsStateWithLifecycle()
    val lives by viewModel.lives.collectAsStateWithLifecycle()
    val highlightedIndex by viewModel.highlightedIndex.collectAsStateWithLifecycle()
    val gridBlocks by viewModel.gridBlock.collectAsStateWithLifecycle()
    val userOptions by viewModel.userOptions.collectAsStateWithLifecycle()
    val isPaused by viewModel.isPaused.collectAsStateWithLifecycle()
    val isGameOver by viewModel.gameOver.collectAsStateWithLifecycle()
    val showNextPlayerDialog by viewModel.showNextPlayerDialog.collectAsStateWithLifecycle()
    val gameResult by viewModel.gameResult.collectAsStateWithLifecycle()
    val currentPlayer by viewModel.currentPlayer.collectAsStateWithLifecycle()
    val isMemorizationPhase by viewModel.isMemorizationPhase.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // Init Game
    LaunchedEffect(Unit) {
        viewModel.startGame(difficulty, gameMode)
    }

    // Handle Vibration
    LaunchedEffect(Unit) {
        viewModel.vibrationEffect.collect {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }
        }
    }

    // Pass data to Stateless UI
    TapToBeatGameContent(
        score = score,
        lives = lives,
        highlightedIndex = highlightedIndex,
        gridBlocks = gridBlocks,
        userOptions = userOptions,
        isPaused = isPaused,
        isGameOver = isGameOver,
        showNextPlayerDialog = showNextPlayerDialog,
        gameResult = gameResult,
        currentPlayer = currentPlayer,
        gameMode = gameMode,
        onPause = viewModel::pauseGame,
        onResume = viewModel::resumeGame,
        onRetry = viewModel::retry,
        onOptionClicked = viewModel::onOptionClicked,
        onNextPlayer = viewModel::startNextPlayerTurn,
        isMemorizationPhase = isMemorizationPhase,
        onBack = onBack
    )
}

// --------------------------------------------------------------------------
// 2. STATELESS CONTENT (Pure UI - Can be Previewed)
// --------------------------------------------------------------------------
@Composable
fun TapToBeatGameContent(
    score: Int,
    lives: Int,
    highlightedIndex: Int,
    gridBlocks: List<Block>,
    userOptions: List<Block>,
    isPaused: Boolean,
    isGameOver: Boolean,
    showNextPlayerDialog: Boolean,
    gameResult: String,
    currentPlayer: Int,
    gameMode: GameMode,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRetry: () -> Unit,
    onOptionClicked: (String) -> Unit,
    onNextPlayer: () -> Unit,
    isMemorizationPhase: Boolean,
    onBack: () -> Unit
) {
    // --- DIALOGS ---

    if (isPaused) {
        AlertDialog(
            onDismissRequest = { /* Force explicit resume */ },
            title = { Text("Game Paused") },
            text = { Text("Take a breath and get ready!") },
            confirmButton = {
                Button(onClick = onResume) { Text("Resume") }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Quit")
                }
            }
        )
    }

    if (showNextPlayerDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Player 1 Finished!") },
            text = {
                Column {
                    Text("Score: $score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Pass the phone to Player 2.")
                }
            },
            confirmButton = {
                Button(onClick = onNextPlayer) { Text("Player 2 Start") }
            }
        )
    }

    if (isGameOver) {
        GameOverDialog(
            resultText = gameResult,
            onRetry = onRetry,
            onBack = onBack
        )
    }

    // --- MAIN LAYOUT ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lives
            Text(
                text = "Lives: $lives",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            // Player Badge
            if (gameMode == GameMode.TwoPlayer) {
                Text(
                    text = "P$currentPlayer",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Blue,
                    modifier = Modifier
                        .border(2.dp, Color.Blue, CircleShape)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            // Score & Pause
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Score: $score",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00C853)
                )
                IconButton(onClick = onPause) {
                    Icon(
                        painter = painterResource(R.drawable.ic_go),
                        contentDescription = "Pause",
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // THE GRID
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gridBlocks.size) { index ->
                val block = gridBlocks[index]
                val isActive = index == highlightedIndex

                // Animation
                val borderColor by animateColorAsState(
                    targetValue = if (isActive) Color(0xFFFFD700) else Color.LightGray,
                    animationSpec = tween(150), label = "border"
                )
                val borderWidth = if (isActive) 4.dp else 1.dp

                // Card
                Column(
                    modifier = Modifier
                        .height(90.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = block.image),
                        contentDescription = block.text,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = block.text,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.Black else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // USER OPTIONS (Buttons)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(userOptions.size) { index ->
                val block = userOptions[index]
                Button(
                    onClick = { onOptionClicked(block.text) },
                    enabled = !isMemorizationPhase,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(80.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = block.image),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = block.text,
                            fontSize = 14.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// --------------------------------------------------------------------------
// 3. HELPER COMPONENTS
// --------------------------------------------------------------------------
@Composable
fun GameOverDialog(resultText: String, onRetry: () -> Unit, onBack: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GAME OVER",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = resultText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Exit")
                    }
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                    ) {
                        Text("Play Again")
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------------
// 4. PREVIEW (Now fully working!)
// --------------------------------------------------------------------------
@Preview(showBackground = true)
@Composable
private fun TapToBeatPreview() {
    // Create Dummy Data
    val dummyBlock = Block(R.drawable.ic_launcher_foreground, "Cat") // Ensure this resource exists or use a generic one
    val dummyList = List(8) { dummyBlock }

    TapToBeatGameContent(
        score = 150,
        lives = 3,
        highlightedIndex = 2, // Simulates the 3rd block being lit up
        gridBlocks = dummyList,
        userOptions = dummyList.take(4),
        isPaused = false, // Toggle to true to see Pause Dialog in preview
        isGameOver = false,
        showNextPlayerDialog = false,
        gameResult = "",
        currentPlayer = 1,
        gameMode = GameMode.TwoPlayer,
        onPause = {},
        onResume = {},
        onRetry = {},
        onOptionClicked = {},
        onNextPlayer = {},
        isMemorizationPhase = false,
        onBack = {}
    )
}
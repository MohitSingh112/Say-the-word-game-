package com.mohit.saythewordgameonbeat.ui.screen.chooseDifficultyScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.emuns.Difficulty
import com.mohit.saythewordgameonbeat.emuns.GameMode
import com.mohit.saythewordgameonbeat.ui.components.CartoonyText
import com.mohit.saythewordgameonbeat.ui.components.GameCard


@Composable
fun ChooseDifficultyScreen(
    modifier: Modifier = Modifier,
    viewModel: ChooseDifficultyViewModel = hiltViewModel(),
    gameMode: GameMode,
    onBack: () -> Unit,
    onStartClicked: (Difficulty) -> Unit
) {

    val difficulty by viewModel.difficulty.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetState() // Clean start every time
    }



    ChooseDifficultyContent(
        modifier = modifier,
        onBack = onBack,
        difficulty = difficulty,
        onEasyClicked = { viewModel.onDifficultyChanged(Difficulty.Easy) },
        onMediumClicked = { viewModel.onDifficultyChanged(Difficulty.Medium) },
        onHardClicked = { viewModel.onDifficultyChanged(Difficulty.Hard)},
        onNightmareClicked = { viewModel.onDifficultyChanged(Difficulty.NightMare) },
        gameMode = gameMode,
        onStartClicked = {onStartClicked(difficulty)}
    )
}

@Composable
fun ChooseDifficultyContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    gameMode: GameMode,
    difficulty: Difficulty,
    onEasyClicked: () -> Unit,
    onMediumClicked: () -> Unit,
    onHardClicked: () -> Unit,
    onNightmareClicked: () -> Unit,
    onStartClicked: () -> Unit
){

    val bgColor = if(GameMode.SinglePlayer == gameMode) Color(0xFF00D9FF)
    else Color(0xFFFCE4EC)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color =  bgColor )
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            //back button
            GameCard(
                color = Color.White,
                borderSize = 1,
                cornerRadius = 8,
                padding = 4,
                onClick = onBack
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(28.dp)
                )
            }

            //Title  text
            CartoonyText(text = "Choose Difficulty",
                fontSize = 24,
                textColor = Color.White,
                family = FontFamily(Font(R.font.luckiest_guy)),
                outlineColor = Color.Black,

            )

            Spacer(modifier = Modifier.size(20.dp))

        }

        Spacer(modifier = Modifier.size(40.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {


            DifficultyCard(
                text = "Easy",
                isSelected = difficulty == Difficulty.Easy,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Green,
                selectedBackgroundColor = Color.Green,
                unselectedBackgroundColor = Color.White,
                onClick = onEasyClicked


            )

            DifficultyCard(
                text = "Medium",
                isSelected = difficulty == Difficulty.Medium,
                selectedTextColor = Color.White,
                unselectedTextColor = Color(0xFFFFD700),
                selectedBackgroundColor = Color(0xFFFFD700),
                unselectedBackgroundColor = Color.White,
                onClick = onMediumClicked



            )

            DifficultyCard(
                text = "Hard",
                isSelected = difficulty == Difficulty.Hard,
                selectedTextColor = Color.White,
                unselectedTextColor = Color(0xFFFF3D00),
                selectedBackgroundColor = Color(0xFFFF3D00) ,
                unselectedBackgroundColor = Color.White,
                onClick = onHardClicked

            )
        }

        Spacer(modifier = Modifier.size(40.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            DifficultyCard(
                text = "NightMare",
                isSelected = difficulty == Difficulty.NightMare,
                selectedTextColor = Color.White,
                unselectedTextColor = Color(0xFF9C27FF)  ,
                selectedBackgroundColor = Color(0xFF9C27FF) ,
                unselectedBackgroundColor = Color.White,
                onClick = onNightmareClicked



            )

        }
        // --- Pushes the button to the bottom ---
        Spacer(modifier = Modifier.weight(1f))

        // --- START BUTTON ---
        val isButtonEnabled = difficulty != Difficulty.None
        val buttonColor = Color(0xFFFFD700)

        Button(
            onClick = onStartClicked,
            enabled = isButtonEnabled,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = 2.dp,
                color = if (isButtonEnabled) Color.Black else Color.Black.copy(alpha = 0.3f)
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.Black,
                disabledContainerColor = buttonColor.copy(alpha = 0.5f), // Slightly transparent/faded when disabled
                disabledContentColor = Color.Black.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            CartoonyText(
                text = "START",
                fontSize = 24,
                textColor = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.7f),
                family = FontFamily(Font(R.font.luckiest_guy)),
                outlineColor = if (isButtonEnabled) Color.Black else Color.Black.copy(alpha = 0.5f)
            )
        }
    }




}

@Preview(showBackground = true)
@Composable
private fun Preview() {

    ChooseDifficultyContent(
        onBack = {},
        onEasyClicked = {},
        onHardClicked = {},
        onMediumClicked = {},
        onNightmareClicked = {},
        difficulty = Difficulty.None,
        gameMode = GameMode.TwoPlayer,
        onStartClicked = {}
    )



}
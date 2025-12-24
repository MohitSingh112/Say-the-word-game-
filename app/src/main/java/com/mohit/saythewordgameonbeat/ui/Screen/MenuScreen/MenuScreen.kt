package com.mohit.saythewordgameonbeat.ui.Screen.MenuScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.emuns.GameMode
import com.mohit.saythewordgameonbeat.ui.Components.CartoonyText
import com.mohit.saythewordgameonbeat.ui.Components.GameCard
import com.mohit.saythewordgameonbeat.ui.theme.SayTheWordGameOnBeatTheme

@Composable
fun MenuScreen( viewModel: MenuViewModel = hiltViewModel()
) {

    val currentMode = viewModel.currentGameMode.collectAsStateWithLifecycle()

    MenuContent(
        currentMode = currentMode.value,
        onModeChanged = viewModel::onGameModeChanged
    )



}

@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    currentMode: GameMode,
    onModeChanged: (GameMode) -> Unit
) {



    val bgColor = if(GameMode.SinglePlayer == currentMode) Color.Cyan
    else Color(0xFFFCE4EC)


    Box(modifier = Modifier
        .fillMaxSize()
        .background(bgColor)
        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 50.dp),
        contentAlignment = Alignment.BottomCenter
    )
    {

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        // Top - Section 1
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        )
        {
            //Top text
            CartoonyText(
                text = "Say The Word On Beat",
                fontSize = 24,
                textColor = Color(0xFFFFD700),
                outlineColor = Color.Black,
                family = FontFamily(Font(R.font.luckiest_guy)),
            )

            //setting
            GameCard(
                color = Color.White,
                borderSize = 1,
                cornerRadius = 8,
                padding = 8,
                onClick = {} //ToDo( add on click event)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = "Settings",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(20.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            // Beat Speak
            GameOptionCard(
                bgColor = Color(0xFF00E676),
                text = "Beat Speak",
                textColor = Color.Yellow,
                image = R.drawable.ic_microphone
            ) {}//Todo

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            // Tap to Beat
            GameOptionCard(
                bgColor = Color(0xFFFF6EC7),
                text = "Tap to Beat",
                textColor = Color(0xFF00D9FF),
                image = R.drawable.ic_tap_to_beat
            ) {}//Todo

            Spacer(
                modifier = Modifier.size(16.dp)
            )

        }

    }

        BottomBar(
            currentMode = currentMode,
            onModeSelected = onModeChanged
        )


    }

}

@Composable
fun BottomBar(
    currentMode: GameMode,
    onModeSelected: (GameMode) -> Unit
) {

    Card(
        modifier = Modifier.wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, Color.Black),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Single Player
            BottomItems(
                isSelected = currentMode == GameMode.SinglePlayer,
                image = R.drawable.single_player,
                title = "Single Player"
            )
            {
                onModeSelected(GameMode.SinglePlayer)
            }
            // Two Players
            BottomItems(isSelected = currentMode == GameMode.TwoPlayer,
                image = R.drawable.two_players,
                title = "Two Players"
            )
            {
                onModeSelected(GameMode.TwoPlayer)
            }
        }

    }
}



@Composable
fun BottomItems(
    isSelected: Boolean = false,
    image: Int = R.drawable.outline_account_child_invert_24,
    title: String = "Single Player",
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(true, onClick = onClick)
            .wrapContentSize()
            .padding(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if(isSelected)Color.Black else Color.White
        ),
        colors = CardDefaults.cardColors(containerColor = if(isSelected) Color.Yellow else Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "Bottom Bar Icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(20.dp)

            )

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.luckiest_guy))
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    SayTheWordGameOnBeatTheme {
        MenuContent(currentMode = GameMode.SinglePlayer, onModeChanged = {})
    }
}
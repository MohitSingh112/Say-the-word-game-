package com.mohit.saythewordgameonbeat.ui.Screen.MenuScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.ui.Components.CartoonyText
import com.mohit.saythewordgameonbeat.ui.Components.GameCard
import com.mohit.saythewordgameonbeat.ui.theme.SayTheWordGameOnBeatTheme

@Composable
fun GameOptionCard(modifier: Modifier = Modifier,
                   image: Int,
                   bgColor: Color,
                   text: String ,
                   textColor: Color = Color.White,
                   onClick: () -> Unit,
) {

    GameCard(
        color = bgColor,
        borderSize = 4,
        cornerRadius = 16,
        padding = 16,
        onClick = onClick
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {

            CartoonyText(
                text = text,
                family = FontFamily(Font(R.font.luckiest_guy)),
                fontSize = 30,
                textColor = textColor
            )

            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(80.dp)
            )

        }


    }

}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    SayTheWordGameOnBeatTheme() {
        GameOptionCard(bgColor = Color.Cyan, text = "Beat Speak" , image = R.drawable.single_player, textColor = Color.Yellow){}
    }
}
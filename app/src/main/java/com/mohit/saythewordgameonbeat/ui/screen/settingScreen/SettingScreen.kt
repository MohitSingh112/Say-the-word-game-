package com.mohit.saythewordgameonbeat.ui.screen.settingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mohit.saythewordgameonbeat.ui.components.CartoonyText
import com.mohit.saythewordgameonbeat.ui.components.GameCard

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onShareClicked: () -> Unit,
    onFeedbackClicked: () -> Unit,
    onBack: () -> Unit,
) {

    SettingContent(
        modifier = modifier,
        onBack = onBack,
        onShareClicked = onShareClicked,
        onFeedbackClicked = onFeedbackClicked
    )

}

@Composable
fun SettingContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onShareClicked: () -> Unit,
    onFeedbackClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Cyan)
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
            CartoonyText(
                text = "Setting",
                fontSize = 24,
                textColor = Color.White,
                family = FontFamily(Font(R.font.luckiest_guy)),
                outlineColor = Color.Black,

                )

            Spacer(modifier = Modifier.size(20.dp))

        }

        Spacer(modifier = Modifier.size(40.dp))

        GameCard(
            color = Color.White,
            borderSize = 1,
            cornerRadius = 16,
            padding = 4,
            clickable = false,
            onClick = {}
            ) {

            SettingItems(
                title = "Share App",
                icon = R.drawable.ic_share,
                onClick = onShareClicked
            )

            SettingItems(
                title = "Feedback",
                icon = R.drawable.ic_feedback,
                onClick = onFeedbackClicked
            )

        }
    }
}

@Composable
fun SettingItems(
    title: String,
    icon: Int,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )

        CartoonyText(
            text = title,
            fontSize = 24,
            family = FontFamily(Font(R.font.luckiest_guy))
        )

        Spacer(modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.size(20.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_go),
            contentDescription = null,
            modifier = Modifier.size(15.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SettingContent(
        onBack = {},
        onShareClicked = {},
        onFeedbackClicked = {}
    )
}
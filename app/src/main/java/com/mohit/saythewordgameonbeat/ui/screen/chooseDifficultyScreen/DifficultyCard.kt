package com.mohit.saythewordgameonbeat.ui.screen.chooseDifficultyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.ui.components.CartoonyText
import com.mohit.saythewordgameonbeat.ui.theme.SayTheWordGameOnBeatTheme



@Composable
fun DifficultyCard(
    text: String,
    isSelected: Boolean,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    selectedBackgroundColor: Color,
    unselectedBackgroundColor: Color,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) selectedBackgroundColor else unselectedBackgroundColor
    val textColor = if (isSelected) selectedTextColor else unselectedTextColor

    // Main card container with shadow and border
    Box(
        contentAlignment = Alignment.Center, // Ensures content stays centered when card is at min size
        modifier = Modifier
            // 1. ADDED: Forces the card to be at least 100dp wide (Fits "Medium")
            // If text is longer, it will expand automatically.
            .defaultMinSize(minWidth = 100.dp, minHeight = 80.dp)
            .shadow(
                elevation = 0.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black,
                ambientColor = Color.Black
            )
            // Custom shadow implementation
            .background(Color.Black, RoundedCornerShape(12.dp))
            .padding(bottom = 4.dp, end = 2.dp) // Shadow offset
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Radio button
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .padding(4.dp)
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Text with outline
            CartoonyText(
                text = text,
                textColor = textColor,
                outlineColor = Color.Black,
                fontSize = 16,
                family = FontFamily(Font(R.font.luckiest_guy)),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDifficultySelection() {
    SayTheWordGameOnBeatTheme {
        DifficultyCard(
            text = "Medium", // Try changing this to "Easy" to see it keep the width
            isSelected = false,
            selectedTextColor = Color.White,
            unselectedTextColor = Color.Green,
            selectedBackgroundColor = Color.Green,
            unselectedBackgroundColor = Color.White,
            onClick = {}
        )
    }
}
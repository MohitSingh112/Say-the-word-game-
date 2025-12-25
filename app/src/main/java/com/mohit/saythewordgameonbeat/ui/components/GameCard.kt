package com.mohit.saythewordgameonbeat.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.ui.theme.SayTheWordGameOnBeatTheme

@Composable
fun GameCard(
    color : Color,
    cornerRadius : Int = 16,
    borderSize: Int,
    padding: Int = 4,
    onClick: () -> Unit,
    clickable: Boolean = true,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable(enabled = clickable, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = color),
        border = BorderStroke(borderSize.dp, Color.Black),
        shape = RoundedCornerShape(cornerRadius.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(padding.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SayTheWordGameOnBeatTheme() {
    GameCard(Color.Cyan, borderSize = 6 ,onClick = {}) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.outline_account_child_invert_24),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
        }
}
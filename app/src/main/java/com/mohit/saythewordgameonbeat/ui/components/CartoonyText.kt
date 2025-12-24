package com.mohit.saythewordgameonbeat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mohit.saythewordgameonbeat.R
import com.mohit.saythewordgameonbeat.ui.theme.SayTheWordGameOnBeatTheme

@Composable
fun CartoonyText(
    text: String,
    outlineColor: Color = Color.Black,
    textColor: Color = Color.White,
    fontSize: Int = 48,
    fontWeight: FontWeight = FontWeight.Normal,
    family: FontFamily = FontFamily.Default,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            fontFamily = family,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                color = outlineColor,
                drawStyle = Stroke(
                    width = (fontSize/3).toFloat(),
                    join = StrokeJoin.Round
                )
            )
        )
        Text(
            text = text,
            fontFamily = family,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                color = textColor
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Prev() {
    val font = FontFamily(Font(R.font.luckiest_guy))

    SayTheWordGameOnBeatTheme() {
        CartoonyText("Say The Word Game On Beat", fontSize = 20 ,textColor = Color.Cyan, outlineColor = Color.Black, family = FontFamily(Font(R.font.luckiest_guy)))
    }

}
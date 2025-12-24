package com.mohit.saythewordgameonbeat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mohit.saythewordgameonbeat.R


// 1. Define the Custom Font Family
val LuckiestGuyFont = FontFamily(
    Font(R.font.luckiest_guy, FontWeight.Normal)
)

// 2. Set up Material Typography
val Typography = Typography(
    // USE THIS for your big headers (The "Cartoony" look)
    displayLarge = TextStyle(
        fontFamily = LuckiestGuyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = LuckiestGuyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    titleLarge = TextStyle(
        fontFamily = LuckiestGuyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp, // Good size for game prompts
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    // KEEP THIS as Default for readability (instructions, lists)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
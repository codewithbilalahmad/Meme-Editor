package com.muhammad.memes.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography: Typography
    @Composable get() = Typography(
        displayLarge = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = Fonts.Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
    )
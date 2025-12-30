package com.muhammad.memes.core.theme

import androidx.compose.runtime.*
import com.muhammad.memes.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object Fonts{
    val Manrope @Composable get() = FontFamily(
        Font(resId = R.font.manrope, weight = FontWeight.Normal)
    )
    val Impact @Composable get() = FontFamily(
        Font(resId = R.font.impact, weight = FontWeight.Normal)
    )
}
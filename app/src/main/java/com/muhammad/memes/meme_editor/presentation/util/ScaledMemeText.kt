package com.muhammad.memes.meme_editor.presentation.util

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import com.muhammad.memes.meme_editor.presentation.MemeText

@Immutable
data class ScaledMemeText(
    val text : String,
    val scaledOffset : Offset,
    val scaledFontSizePx : Float,
    val strokeWidth : Float,
    val constraintsWidth : Int,
    val textPaddingX : Float,
    val textPaddingY : Float,
    val rotation : Float,
    val scale : Float,
    val originalText : MemeText
)
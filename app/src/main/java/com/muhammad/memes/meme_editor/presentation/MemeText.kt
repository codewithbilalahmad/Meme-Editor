package com.muhammad.memes.meme_editor.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class MemeText(
    val id : String,
    val text : String,
    val fontSize : Float = 36f,
    val offsetRatioX : Float = 0f,
    val offsetRatioY : Float = 0f,
    val rotation : Float = 0f,
    val scale : Float = 1f
)
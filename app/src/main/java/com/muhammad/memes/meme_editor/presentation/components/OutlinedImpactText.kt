package com.muhammad.memes.meme_editor.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun OutlinedImpactText(
    modifier: Modifier = Modifier,
    text: String,
    strokeTextStyle: TextStyle,
    fillTextStyle: TextStyle,
) {
    Box(modifier = modifier) {
        Text(text = text, style = strokeTextStyle)
        Text(text = text, style = fillTextStyle)
    }
}
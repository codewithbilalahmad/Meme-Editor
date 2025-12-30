package com.muhammad.memes.meme_editor.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import com.muhammad.memes.meme_editor.presentation.util.rememberFillTextStyle
import com.muhammad.memes.meme_editor.presentation.util.rememberStrokeTextStyle

@Composable
fun OutlinedImpactTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    strokeTextStyle: TextStyle = rememberStrokeTextStyle(),
    fillTextStyle: TextStyle = rememberFillTextStyle(),
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
) {
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val constraints = calculateTextConstraints(maxWidth, maxHeight)
    val textLayoutResult = remember(text, strokeTextStyle, constraints) {
        measurer.measure(
            text = text, style = strokeTextStyle, constraints = constraints
        )
    }
    val textBoundingSize = with(density){
        DpSize(
            width = textLayoutResult.size.width.toDp(),
            height = textLayoutResult.size.height.toDp()
        )
    }
    BasicTextField(
        value = text,
        onValueChange = { newValue ->
            onTextChange(newValue.uppercase())
        },
        cursorBrush = SolidColor(Color.White),
        singleLine = false,
        textStyle = fillTextStyle,
        decorationBox = {innerTextField ->
            Text(text = text, style = strokeTextStyle)
            innerTextField()
        }, modifier = modifier.size(textBoundingSize))
}

@Composable
private fun calculateTextConstraints(maxWidth: Dp?, maxHeight: Dp?) : Constraints{
    val density = LocalDensity.current
    return with(density){
        Constraints(
            maxWidth = maxWidth?.roundToPx() ?: Int.MAX_VALUE,
            maxHeight = maxHeight?.roundToPx() ?: Int.MAX_VALUE
        )
    }
}
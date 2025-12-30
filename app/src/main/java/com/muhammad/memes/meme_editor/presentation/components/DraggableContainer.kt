package com.muhammad.memes.meme_editor.presentation.components

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.muhammad.memes.meme_editor.presentation.MemeText
import com.muhammad.memes.meme_editor.presentation.TextBoxInteractionState
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DraggableContainer(
    modifier: Modifier = Modifier,
    children: List<MemeText>,
    textBoxInteractionState: TextBoxInteractionState,
    onChildTransformChange: (id: String, offset: Offset, scale: Float, rotation: Float) -> Unit,
    onChildClick: (id: String) -> Unit,
    onChildDoubleClick: (id: String) -> Unit,
    onChildTextChange: (id: String, text: String) -> Unit,
    onChildDeleteClick: (id: String) -> Unit,
) {
    val density = LocalDensity.current
    BoxWithConstraints(modifier = modifier) {
        val parentWidth = constraints.maxWidth
        val parentHeight = constraints.maxHeight
        children.forEach { child ->
            var childWidth by remember(child.id) { mutableIntStateOf(0) }
            var childHeight by remember(child.id) { mutableIntStateOf(0) }
            val transformableState =
                rememberTransformableState { scaleChange, panOffset, rotationChange ->
                    val newRotation = child.rotation + rotationChange
                    val angle = newRotation * PI.toFloat() / 180f
                    val cos = cos(angle)
                    val sin = sin(angle)
                    val rotatedPanX = panOffset.x * cos - panOffset.y * sin
                    val rotatedPanY = panOffset.x * sin + panOffset.y * cos
                    val scaledWidth = childWidth * child.scale
                    val scaleHeight = childHeight * child.scale
                    val visualWidth = abs(scaledWidth * cos) + abs(scaleHeight * sin)
                    val visualHeight = abs(scaledWidth * sin) + abs(scaleHeight * cos)
                    val scaleOffsetX = (scaledWidth - childWidth) / 2
                    val scaleOffsetY = (scaleHeight - scaleHeight) / 2
                    val rotatedOffsetX = (visualWidth - scaledWidth) / 2
                    val rotatedOffsetY = (visualHeight - scaleHeight) / 2

                    val minX = scaleOffsetX + rotatedOffsetX
                    val maxX = parentWidth - childWidth - scaleOffsetX - rotatedOffsetX
                    val minY = scaleOffsetY + rotatedOffsetY
                    val maxY = parentHeight - childHeight - scaleOffsetY - rotatedOffsetY

                    val newScale = (child.scale * scaleChange).coerceIn(0.5f, 2f)
                    val newOffset = Offset(
                        x = (child.offsetRatioX * parentWidth + child.scale * rotatedPanX).coerceIn(
                            minimumValue = minOf(minX, maxX),
                            maximumValue = maxOf(minX, maxX)
                        ),
                        y = (child.offsetRatioY * parentHeight + child.scale * rotatedPanY).coerceIn(
                            minimumValue = minOf(minY, maxY),
                            maximumValue = maxOf(minY, maxY)
                        )
                    )
                    onChildTransformChange(child.id, newOffset, newScale, newRotation)
                }
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->
                        childWidth = size.width
                        childHeight = size.height
                    }
                    .graphicsLayer {
                        translationX = child.offsetRatioX * parentWidth
                        translationY = child.offsetRatioY * parentHeight
                        rotationZ = child.rotation
                        scaleX = child.scale
                        scaleY = child.scale
                    }
                    .transformable(transformableState)
            ) {
                MemeTextBox(
                    memeText = child,
                    textBoxInteractionState = textBoxInteractionState,
                    onClick = {
                        onChildClick(child.id)
                    }, onDoubleClick = {
                        onChildDoubleClick(child.id)
                    }, onTextChange = { text ->
                        onChildTextChange(child.id, text)
                    }, onDeleteClick = {
                        onChildDeleteClick(child.id)
                    }, maxHeight = with(density) {
                        (parentHeight / child.scale).toDp()
                    }, maxWidth = with(density) {
                        (parentWidth / child.scale).toDp()
                    }
                )
            }
        }
    }
}
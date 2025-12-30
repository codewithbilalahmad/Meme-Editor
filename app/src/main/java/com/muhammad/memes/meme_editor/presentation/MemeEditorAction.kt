package com.muhammad.memes.meme_editor.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.muhammad.memes.core.presentation.MemeTemplate

sealed interface MemeEditorAction{
    data object OnGoBack : MemeEditorAction
    data object OnConfirmLeaveWithoutSaving : MemeEditorAction
    data object OnDismissLeaveWithoutSaving : MemeEditorAction
    data class OnSaveMemeClick(val memeTemplate: MemeTemplate) : MemeEditorAction
    data object OnTapOutsideSelectedText : MemeEditorAction
    data object OnAddTextClick : MemeEditorAction
    data class OnSelectMemeText(val id : String) : MemeEditorAction
    data class OnEditMemeText(val id : String) : MemeEditorAction
    data class OnMemeTextChange(val id : String, val text : String) : MemeEditorAction
    data class OnDeleteMemeText(val id : String) : MemeEditorAction
    data class OnMemeTextTransformChange(
        val id : String,
        val offset : Offset,
        val rotation : Float,
        val scale : Float
    ) : MemeEditorAction
    data class OnContainerSizeChange(val size : IntSize) : MemeEditorAction
}
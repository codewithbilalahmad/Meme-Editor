@file:OptIn(ExperimentalUuidApi::class)

package com.muhammad.memes.meme_editor.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.memes.core.presentation.MemeTemplate
import com.muhammad.memes.meme_editor.domain.MemeExporter
import com.muhammad.memes.meme_editor.domain.SaveToStorageStrategy
import com.muhammad.memes.meme_editor.domain.ShareSheet
import com.muhammad.memes.meme_editor.presentation.util.getImageBytesFromResources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MemeEditorViewModel(
    private val saveToStorageStrategy: SaveToStorageStrategy,
    private val memeExporter: MemeExporter,
    private val shareSheet: ShareSheet,
) : ViewModel() {
    private val _state = MutableStateFlow(MemeEditorState())
    val state = _state.asStateFlow()
    fun onAction(action: MemeEditorAction) {
        when (action) {
            MemeEditorAction.OnAddTextClick -> onAddTextClick()
            MemeEditorAction.OnConfirmLeaveWithoutSaving -> onConfirmLeaveWithoutSaving()
            is MemeEditorAction.OnContainerSizeChange -> onContainerSizeChange(action.size)
            is MemeEditorAction.OnDeleteMemeText -> onDeleteMemeText(action.id)
            MemeEditorAction.OnDismissLeaveWithoutSaving -> onDismissLeaveWithoutSaving()
            is MemeEditorAction.OnEditMemeText -> onEditMemeText(action.id)
            MemeEditorAction.OnGoBack -> onGoBack()
            is MemeEditorAction.OnMemeTextChange -> onMemeTextChange(
                id = action.id,
                text = action.text
            )

            is MemeEditorAction.OnMemeTextTransformChange -> onMemeTextTransformChange(
                id = action.id,
                offset = action.offset,
                scale = action.scale,
                rotation = action.rotation
            )

            is MemeEditorAction.OnSaveMemeClick -> onSaveMemeClick(action.memeTemplate)
            is MemeEditorAction.OnSelectMemeText -> onSelectMemeText(action.id)
            MemeEditorAction.OnTapOutsideSelectedText -> onTapOutsideSelectedText()
        }
    }

    private fun onContainerSizeChange(size: IntSize) {
        _state.update { it.copy(templateSize = size) }
    }

    private fun onDeleteMemeText(id: String) {
        _state.update {
            it.copy(
                memeTexts = it.memeTexts.filter { memeText ->
                    memeText.id != id
                }
            )
        }
    }

    private fun onConfirmLeaveWithoutSaving() {
        _state.update { it.copy(hasLeftEditor = true) }
    }

    private fun onDismissLeaveWithoutSaving() {
        _state.update { it.copy(isLeavingWithoutSaving = false) }
    }

    private fun onEditMemeText(id: String) {
        _state.update { it.copy(textBoxInteractionState = TextBoxInteractionState.Editing(id)) }
    }

    private fun onGoBack() {
        if (state.value.memeTexts.isEmpty()) {
            _state.update { it.copy(hasLeftEditor = true) }
        } else {
            _state.update { it.copy(isLeavingWithoutSaving = true) }
        }
    }

    private fun onMemeTextChange(id: String, text: String) {
        _state.update {
            it.copy(
                memeTexts = it.memeTexts.map { memeText ->
                    if (memeText.id == id) {
                        memeText.copy(text = text)
                    } else memeText
                }
            )
        }
    }

    private fun onMemeTextTransformChange(
        id: String,
        offset: Offset,
        rotation: Float,
        scale: Float,
    ) {
        _state.update {
            val (width, height) = it.templateSize
            it.copy(
                memeTexts = it.memeTexts.map { memeText ->
                    if (memeText.id == id) {
                        memeText.copy(
                            offsetRatioX = offset.x / width,
                            offsetRatioY = offset.y / height, rotation = rotation, scale = scale
                        )
                    } else memeText
                }
            )
        }
    }

    private fun onSaveMemeClick(memeTemplate: MemeTemplate) {
        viewModelScope.launch {
            memeExporter.exportMeme(
                backgroundImageBytes = getImageBytesFromResources(memeTemplate.drawable),
                memeTexts = state.value.memeTexts,
                saveToStorageStrategy = saveToStorageStrategy,
                templateSize = state.value.templateSize
            ).onSuccess { filePath ->
                shareSheet.shareFile(filePath)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    private fun onSelectMemeText(id: String) {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.Selected(id)
            )
        }
    }

    private fun onAddTextClick() {
        _state.update {
            val memeText = MemeText(
                id = Uuid.random().toString(),
                text = "TAP TO EDIT!",
                offsetRatioX = 0.25f,
                offsetRatioY = 0.25f
            )
            it.copy(memeTexts = it.memeTexts + memeText)
        }
    }

    private fun onTapOutsideSelectedText() {
        _state.update { it.copy(textBoxInteractionState = TextBoxInteractionState.None) }
    }
}
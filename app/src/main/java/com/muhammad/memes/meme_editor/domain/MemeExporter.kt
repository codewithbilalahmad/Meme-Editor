package com.muhammad.memes.meme_editor.domain

import androidx.compose.ui.unit.IntSize
import com.muhammad.memes.meme_editor.presentation.MemeText
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MemeExporter{
    @OptIn(ExperimentalUuidApi::class)
    suspend fun exportMeme(
        backgroundImageBytes : ByteArray,
        memeText : List<MemeText>,
        templateSize : IntSize,
        saveToStorageStrategy: SaveToStorageStrategy,
        fileName : String = "meme_${Uuid.random().toString()}.jpg"
    )
}
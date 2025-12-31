package com.muhammad.memes.meme_editor.domain

interface ShareSheet{
    suspend fun shareFile(filePath : String)
}
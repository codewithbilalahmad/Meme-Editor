package com.muhammad.memes.meme_editor.domain

interface SaveToStorageStrategy{
    fun getFilePath(fileName : String) : String
}
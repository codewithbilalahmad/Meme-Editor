package com.muhammad.memes.meme_editor.data

import android.content.Context
import com.muhammad.memes.meme_editor.domain.SaveToStorageStrategy
import java.io.File

class CacheStorageStrategy(
    private val context : Context
) : SaveToStorageStrategy{
    override fun getFilePath(fileName: String): String {
        return File(context.cacheDir, fileName).absolutePath
    }
}
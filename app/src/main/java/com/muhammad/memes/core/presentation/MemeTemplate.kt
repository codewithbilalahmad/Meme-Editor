package com.muhammad.memes.core.presentation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.muhammad.memes.R

@Immutable
data class MemeTemplate(
    val id : String,
    @get:DrawableRes val drawable : Int
)

val memeTemplates by lazy {
    val drawableClass = R.drawable::class.java
    drawableClass.fields.filter { it.name.startsWith("meme_template") }.map { field ->
        MemeTemplate(
            id = field.name,
            drawable = field.getInt(null)
        )
    }
}
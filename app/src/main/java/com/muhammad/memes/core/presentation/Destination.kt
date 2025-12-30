package com.muhammad.memes.core.presentation

import kotlinx.serialization.*

sealed interface Destination{
    @Serializable
    data object MemeGallery : Destination
    @Serializable
    data class MemeEditor(
        val templateId : Int
    ) : Destination
}
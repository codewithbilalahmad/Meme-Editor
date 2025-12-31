package com.muhammad.memes.di

import com.muhammad.memes.MemeApplication
import com.muhammad.memes.meme_editor.data.AndroidMemeExporter
import com.muhammad.memes.meme_editor.data.CacheStorageStrategy
import com.muhammad.memes.meme_editor.data.MemeShareSheet
import com.muhammad.memes.meme_editor.domain.MemeExporter
import com.muhammad.memes.meme_editor.domain.SaveToStorageStrategy
import com.muhammad.memes.meme_editor.domain.ShareSheet
import com.muhammad.memes.meme_editor.presentation.MemeEditorViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { MemeApplication.INSTANCE }
    singleOf(::CacheStorageStrategy).bind<SaveToStorageStrategy>()
    singleOf(::AndroidMemeExporter).bind<MemeExporter>()
    singleOf(::MemeShareSheet).bind<ShareSheet>()
    viewModelOf(::MemeEditorViewModel)
}
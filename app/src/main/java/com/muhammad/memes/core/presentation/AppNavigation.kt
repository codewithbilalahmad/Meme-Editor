package com.muhammad.memes.core.presentation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.muhammad.memes.meme_editor.presentation.MemeEditorScreen
import com.muhammad.memes.meme_gallery.MemeGalleryScreen

@Composable
fun AppNavigation() {
    val navHostController= rememberNavController()
    NavHost(navController = navHostController, startDestination = Destination.MemeGallery){
        composable<Destination.MemeGallery>{
            MemeGalleryScreen(onMemeTemplateSelected = {memeTemplate ->
                navHostController.navigate(Destination.MemeEditor(memeTemplate.drawable))
            })
        }
        composable<Destination.MemeEditor>{
            val templateId = it.toRoute<Destination.MemeEditor>().templateId
            val template = remember(templateId) {
                memeTemplates.first {meme -> meme.drawable == templateId }
            }
            MemeEditorScreen(memeTemplate = template, navHostController = navHostController)
        }
    }
}
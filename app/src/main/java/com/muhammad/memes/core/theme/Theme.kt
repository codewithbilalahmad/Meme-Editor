package com.muhammad.memes.core.theme

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    surfaceContainerLowest = Color(0xFF0F0D13),
    surfaceContainerLow = Color(0xFF1D1B20),
    surfaceContainer = Color(0xFF211F26),
    surfaceContainerHigh = Color(0xFF2B2930),
    outline = Color(0xFF79747E),
    primary = Color(0xFF65558F),
    secondary = Color(0xFFCCC2DC),
    onSurface = Color(0xFFE6E0E9),
    primaryContainer = Color(0xFFEADDFF),
    error = Color(0xFFB3261E),
    onPrimary = Color(0xFF21005D),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MemesTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val window = (context as Activity).window
    SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = false
        }
    }
    MaterialExpressiveTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}
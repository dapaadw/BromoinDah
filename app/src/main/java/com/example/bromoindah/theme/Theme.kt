package com.example.bromoindah.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Green60,
    onPrimary = OnPrimaryDark,
    primaryContainer = Green20,
    onPrimaryContainer = Green80,
    secondary = Brown60,
    onSecondary = OnPrimaryLight,
    secondaryContainer = Brown40,
    tertiary = Gold,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Color(0xFF414941),
    onSurfaceVariant = Color(0xFFC1C9BF),
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = OnPrimaryLight,
    primaryContainer = Green80,
    onPrimaryContainer = Green20,
    secondary = Brown40,
    onSecondary = OnPrimaryLight,
    secondaryContainer = Brown80,
    tertiary = Gold,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFFDEE5D9),
    onSurfaceVariant = Color(0xFF424940),
)

@Composable
fun BromoInDahTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

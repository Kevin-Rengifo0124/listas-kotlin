package com.example.tallerlistas.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.tallerlistas.CardDark
import com.example.tallerlistas.DarkBg

private val AppColorScheme = darkColorScheme(
    primary       = Purple80,
    secondary     = Pink80,
    background    = DarkBg,
    surface       = CardDark,
    onPrimary     = Color.White,
    onBackground  = Color(0xFFF0F0FF),
    onSurface     = Color(0xFFF0F0FF),
    error         = Color(0xFFFF6B6B)
)

@Composable
fun ProductAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content     = content
    )
}

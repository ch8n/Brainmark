package dev.ch8n.android.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

@Composable
fun String.parseColor(): Color {
    return if (isEmpty()) {
        MaterialTheme.colors.onSurface
    } else {
        Color(toColorInt())
    }
}


package dev.ch8n.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import dev.ch8n.common.ui.theme.BrainMarkTheme

fun String.parseColor(): Color {
    return Color(toColorInt())
}

@Composable
fun Preview(
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    BrainMarkTheme(isDark) {
        content.invoke()
    }
}
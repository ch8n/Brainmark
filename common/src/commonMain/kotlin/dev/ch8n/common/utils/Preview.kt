package dev.ch8n.common.utils

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.ch8n.common.ui.theme.BrainMarkTheme

@Composable
fun Preview(
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    BrainMarkTheme(isDark) {
        content.invoke()
    }
}

@Composable
fun DevelopmentPreview(
    windowModifier: Modifier = Modifier
        .fillMaxSize()
        .border(5.dp, Color.Magenta)
        .horizontalScroll(rememberScrollState()),
    previewModifier: Modifier = Modifier,
    content: @Composable BoxScope.(isDark: Boolean) -> Unit
) {

    Row(
        modifier = windowModifier
    ) {

        Preview(isDark = true) {
            Box(
                modifier = previewModifier
                    .background(MaterialTheme.colors.surface)
                    .border(5.dp, MaterialTheme.colors.onSurface)
                    .verticalScroll(rememberScrollState())
            ) {
                this.content(true)
            }
        }

        Preview(isDark = false) {
            Box(
                modifier = previewModifier
                    .background(MaterialTheme.colors.surface)
                    .border(5.dp, MaterialTheme.colors.onSurface)
                    .verticalScroll(rememberScrollState())
            ) {
                this.content(false)
            }
        }
    }
}
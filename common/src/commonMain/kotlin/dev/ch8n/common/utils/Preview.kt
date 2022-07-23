package dev.ch8n.common.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    previewModifier: Modifier = Modifier
        .fillMaxWidth()
        .height(400.dp),
    content: @Composable BoxScope.(isDark: Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Preview(isDark = true) {
            Box(
                modifier = previewModifier
                    .background(MaterialTheme.colors.surface)
            ) {
                this.content(true)
            }
        }

        Preview(isDark = false) {
            Box(
                modifier = previewModifier
                    .background(MaterialTheme.colors.surface)
            ) {
                this.content(false)
            }
        }
    }
}
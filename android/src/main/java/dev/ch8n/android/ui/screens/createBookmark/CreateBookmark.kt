package dev.ch8n.android.ui.screens.createBookmark

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ch8n.common.utils.DevelopmentPreview

@Composable
fun PreviewCreateBookmark() {
    DevelopmentPreview { isDark ->
        CreateBookmarkContent(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
        )
    }
}

@Composable
fun CreateBookmarkContent(
    modifier: Modifier
) {

}
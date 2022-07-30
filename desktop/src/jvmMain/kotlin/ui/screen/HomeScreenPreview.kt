package ui.screen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ch8n.common.utils.DevelopmentPreview

@Composable
fun TagScreenPreview() {
    DevelopmentPreview(
        previewModifier = Modifier
            .width(650.dp)
            .fillMaxHeight()
    ) { isDark ->
        Button(onClick = {}) {
            Text("Pokemon")
        }
    }
}
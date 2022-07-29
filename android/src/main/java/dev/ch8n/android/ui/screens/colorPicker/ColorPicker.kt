package dev.ch8n.android.ui.screens.colorPicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ch8n.android.R
import dev.ch8n.common.utils.ColorsUtils
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewColorPicker() {
    DevelopmentPreview { isDark ->
        ColorPicker(
            modifier = Modifier.fillMaxSize(),
            onColorSelected = {

            },
            onClose = {

            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPicker(
    modifier: Modifier,
    onClose: () -> Unit,
    onColorSelected: (color: Color) -> Unit
) {
    val colors = remember { ColorsUtils.colors }

    Column(
        modifier = modifier.background(MaterialTheme.colors.surface)
    ) {


        ToolbarColorPicker(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 48.dp)
                .fillMaxWidth(),
            onClose = onClose
        )

        LazyVerticalGrid(
            cells = GridCells.Adaptive(56.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            colors.forEachIndexed { index, colorList: List<Pair<String, Color>> ->
                items(colorList) { (name, color) ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color, CircleShape)
                            .border(2.dp, MaterialTheme.colors.onSurface, CircleShape)
                            .clickable {
                                onColorSelected.invoke(color)
                            }
                    )
                }
            }

        }

    }
}

@Composable
private fun ToolbarColorPicker(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Pick a Color",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onSurface
        )


        AsyncImage(
            model = R.drawable.close,
            modifier = Modifier
                .size(24.dp)
                .clickable { onClose.invoke() },
            contentDescription = "",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colors.onSurface
            )
        )

    }

}
package dev.ch8n.android.design

import androidx.annotation.IdRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.ch8n.android.utils.parseColor

@Composable
fun Chip(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit = {},
    borderColor: Color = Color.Gray
) {
    val chipColor = remember { "#575757".parseColor() }
    Box(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(chipColor)
            .clickable { onClick.invoke() }
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun IconChip(
    modifier: Modifier,
    resourceId: Int,
    onClick: () -> Unit = {},
    borderColor: Color = Color.Gray
) {
    val chipColor = remember { "#575757".parseColor() }
    Box(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(chipColor)
            .clickable { onClick.invoke() }
    ) {
        AsyncImage(
            model = resourceId,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(16.dp),
            contentScale = ContentScale.FillBounds,
            contentDescription = ""
        )
    }
}
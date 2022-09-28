package dev.ch8n.android.design.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ch8n.android.R
import dev.ch8n.common.utils.AndroidPreview


@Composable
fun PreviewBottomNav() {
    AndroidPreview {
        BottomNavbar(
            modifier = Modifier
                .padding(24.dp)
                .width(240.dp),
            onBookmarkClicked = {},
            onNewBookmarkClicked = {},
            onTagClicked = {}
        )
    }
}

@Composable
fun BottomNavbar(
    modifier: Modifier,
    onTagClicked: () -> Unit,
    onBookmarkClicked: () -> Unit,
    onNewBookmarkClicked: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {

        NavItem(
            modifier = Modifier
                .size(width = 72.dp, height = 42.dp)
                .offset(x = 8.dp)
                .align(Alignment.CenterStart),
            title = "Tags",
            iconResId = R.drawable.tag,
            onClick = onTagClicked
        )

        NavItem(
            modifier = Modifier
                .size(width = 88.dp, height = 42.dp)
                .offset(x = (-4).dp)
                .align(Alignment.CenterEnd),
            title = "Bookmarks",
            iconResId = R.drawable.bookmark,
            onClick = onBookmarkClicked
        )

        Box(
            modifier = Modifier
                .size(64.dp)
                .offset(x = (-8).dp)
                .align(Alignment.Center)
                .background(color = MaterialTheme.colors.secondaryVariant, CircleShape)
                .border(
                    width = 4.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colors.surface
                )
                .clickable(
                    onClick = onNewBookmarkClicked
                )
        ) {
            AsyncImage(
                model = R.drawable.add_bookmark,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colors.surface
                )
            )
        }

    }

}

@Composable
fun NavItem(
    modifier: Modifier,
    title: String,
    @DrawableRes iconResId: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface, MaterialTheme.shapes.large)
            .clickable { onClick.invoke() }
            .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = iconResId,
            contentDescription = "",
            modifier = Modifier.size(16.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colors.secondaryVariant
            )
        )

        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

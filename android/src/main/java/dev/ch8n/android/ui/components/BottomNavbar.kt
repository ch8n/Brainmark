package dev.ch8n.android.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ch8n.android.R
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewBottomNav() {
    DevelopmentPreview { isDark ->
        BottomNavbar(
            modifier = Modifier
                .padding(24.dp)
                .width(240.dp)
        )
    }
}

@Composable
fun BottomNavbar(
    modifier: Modifier,
) {
    Box(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colors.surface)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.secondaryVariant,
                    shape = MaterialTheme.shapes.large
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "Tags",
                iconResId = R.drawable.tag,
                onClick = {

                }
            )

            NavItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "Bookmarks",
                iconResId = R.drawable.bookmark,
                onClick = {

                }
            )
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center)
                .offset(x = (-8).dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.onSurface)
                .border(
                    width = 2.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colors.secondaryVariant
                )
                .clickable {

                }
        ) {
            AsyncImage(
                model = dev.ch8n.android.R.drawable.add_bookmark,
                modifier = Modifier.size(32.dp).align(Alignment.Center),
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
            .padding(8.dp)
            .clickable { onClick.invoke() },
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

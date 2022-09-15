package dev.ch8n.android.design.components

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.theme.black1
import dev.ch8n.common.ui.theme.white2
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewBookmarkCard() {
    DevelopmentPreview { isDark ->
        BookmarkCard(
            bookmark = Bookmark.SAMPLE,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(176.dp),
            onClick = {

            }
        )
    }
}


@Composable
fun BookmarkCard(
    modifier: Modifier,
    bookmark: Bookmark,
    onClick: (bookmark: Bookmark) -> Unit
) {

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset((-4).dp, 4.dp)
                .background(
                    color = MaterialTheme.colors.onSecondary,
                    shape = MaterialTheme.shapes.medium
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onSecondary
                )
                .shadow(4.dp, MaterialTheme.shapes.medium)
                .clickable {
                    onClick.invoke(bookmark)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(black1)
                    .alpha(0.7f)
            ) {
                AsyncImage(
                    model = bookmark.mainImage,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }

            AsyncImage(
                model = bookmark.favIcon,
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(white2),
                contentDescription = "",
                contentScale = ContentScale.Fit,
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = bookmark.siteName,
                    style = MaterialTheme.typography.h4,
                    color = white2
                )

                Text(
                    text = bookmark.title,
                    style = MaterialTheme.typography.h3,
                    color = white2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.size(4.dp))

                Text(
                    text = bookmark.description,
                    style = MaterialTheme.typography.body1,
                    color = white2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

}

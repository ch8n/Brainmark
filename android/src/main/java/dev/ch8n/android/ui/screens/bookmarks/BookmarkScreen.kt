package dev.ch8n.android.ui.screens.bookmarks

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BookmarkCard
import dev.ch8n.android.design.components.BottomNavbar
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.controllers.BookmarkScreenController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewBookmarkScreen(
    componentContext: DefaultComponentContext
) {
    val controller = BookmarkScreenController(
        componentContext = componentContext,
        navigateTo = {},
        navigateBack = {}
    )
    DevelopmentPreview { isDark ->
        BookmarkScreen(controller, {})
    }
}


@Composable
fun BookmarkScreen(
    controller: BookmarkScreenController,
    onSettingsClicked: () -> Unit
) {

    val (bookmarks, setBookmarks) = remember {
        mutableStateOf(
            listOf(
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
                Bookmark.SAMPLE,
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        val (searchQuery, setSearchQuery) = remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
        ) {
            ToolbarBookmark(
                tag = Tags.TAG_KOTLIN,
                onSettingsClicked = onSettingsClicked,
                onTagSwitched = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = setSearchQuery,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Search")
                },
                placeholder = {
                    Text(
                        text = "Search each for keyword  in title and description",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {
                    AsyncImage(
                        model = R.drawable.search,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(24.dp),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colors.onSurface
                        )
                    )
                }
            )

            Spacer(Modifier.size(16.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.onSurface,
                thickness = 0.5.dp
            )

            Spacer(Modifier.size(16.dp))

            TagProgress(
                archivedCount = "5",
                totalCount = "10",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.size(32.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 126.dp)
            ) {

                items(bookmarks) { bookmark ->
                    BookmarkCard(
                        bookmark = bookmark,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .height(176.dp),
                        onClick = {
                            controller.navigateTo(Destinations.PreviewScreen)
                        }
                    )
                }

            }


        }

        BottomNavbar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .width(240.dp),
            onTagClicked = {
                controller.navigateTo(Destinations.TagManager)
            },
            onBookmarkClicked = {},
            onNewBookmarkClicked = {
                controller.navigateTo(Destinations.CreateBookmark)
            }
        )

    }
}

@Composable
fun TagProgress(
    archivedCount: String,
    totalCount: String,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .height(56.dp)
            .border(
                width = 2.dp,
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colors.onSurface,
            )
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$archivedCount/$totalCount Bookmarks",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 24.dp, top = 8.dp)

            )

            Text(
                text = "100%",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(end = 24.dp, top = 8.dp)
            )
        }

        LinearProgressIndicator(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.85f)
                .align(Alignment.BottomCenter),
        )

    }
}


@Composable
private fun ToolbarBookmark(
    tag: Tags,
    onSettingsClicked: () -> Unit,
    onTagSwitched: (tag: Tags) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = R.drawable.brain,
                modifier = Modifier.size(40.dp),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colors.onSurface
                )
            )

            Column(
                modifier = Modifier
                    .offset(y = 4.dp)
                    .clickable { }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = tag.name,
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onSurface
                    )

                    AsyncImage(
                        model = R.drawable.drop_down,
                        modifier = Modifier
                            .size(16.dp),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colors.onSurface
                        )
                    )

                }

                Text(
                    text = "Select from tag dropdown",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }


        AsyncImage(
            model = R.drawable.settings,
            modifier = Modifier
                .size(24.dp)
                .clickable { onSettingsClicked.invoke() },
            contentDescription = "",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colors.onSurface
            )
        )

    }

}
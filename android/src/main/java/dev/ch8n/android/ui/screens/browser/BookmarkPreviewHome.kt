package dev.ch8n.android.ui.screens.browser

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import com.google.accompanist.flowlayout.FlowRow
import dev.ch8n.android.R
import dev.ch8n.android.design.components.TagChip
import dev.ch8n.android.ui.components.ScrollableColumn
import dev.ch8n.android.ui.screens.browser.clients.launchChromeTab
import dev.ch8n.android.utils.toast
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.controllers.PreviewBookmarkHomeController
import dev.ch8n.common.ui.navigation.EmptyNavController
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.ui.navigation.PreviewBookmarkEmbeddedWebDestination
import dev.ch8n.common.ui.navigation.PreviewBookmarkReaderModeDestination
import dev.ch8n.common.utils.AndroidPreview


class AndroidPreviewBookmarkHomeController(
    navController: NavController,
    bookmark: Bookmark
) : PreviewBookmarkHomeController(navController, bookmark) {
    @Composable
    override fun Render() {
        PreviewBookmarkHome(controller = this)
    }
}


@Composable
fun PreviewBrowserScreen(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        AndroidPreviewBookmarkHomeController(
            EmptyNavController(),
            Bookmark.SAMPLE
        )
    }
    AndroidPreview(
        isSplitView = false,
        isDark = true,
    ) {
        PreviewBookmarkHome(controller)
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PreviewBookmarkHome(
    controller: AndroidPreviewBookmarkHomeController,
) {

    val context = LocalContext.current
    val screenState by controller.screenState.collectAsState()

    LaunchedEffect(screenState) {
        val bookmark = screenState.bookmark
        if (bookmark.id == "0") {
            controller.getBookmark()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .alpha(0.4f)
        ) {
            AsyncImage(
                model = screenState.bookmark.mainImage,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }

        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = R.drawable.back,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Start)
                    .clickable { controller.back() },
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Color.White
                )
            )

            Spacer(Modifier.size(24.dp))

            Text(
                text = screenState.bookmark.title,
                style = MaterialTheme.typography.h2.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                screenState.tags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth()
                            .height(35.dp),
                        onTagClicked = {
                            "goto bookmarks with tag filter".toast(context)
                        }
                    )
                }
            }

            OutlineButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                label = "Open Bookmark Notes",
                onClick = {
                    "TODO".toast(context)
                }
            )

            OutlineButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                label = "Edit Bookmark",
                onClick = {
                    "TODO".toast(context)
                }
            )

            OutlineButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                label = if (screenState.bookmark.isArchived) {
                    "Un-archive Bookmark"
                } else {
                    "Archive Bookmark"
                },
                onClick = {
                    controller.archiveBookmark()
                }
            )

            OutlineButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                label = "Open in Reader Mode",
                onClick = {
                    controller.routeTo(
                        PreviewBookmarkReaderModeDestination(screenState.bookmark)
                    )
                }
            )

            OutlineButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                label = "Open in Chrome Tab",
                onClick = {
                    context.launchChromeTab(
                        screenState.bookmark.bookmarkUrl
                    )
                }
            )

            OutlineButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(55.dp),
                label = "Open in WebView",
                onClick = {
                    controller.routeTo(
                        PreviewBookmarkEmbeddedWebDestination(
                            screenState.bookmark
                        )
                    )
                }
            )

        }
    }
}


@Composable
fun OutlineButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.large
            )
            .border(
                2.dp,
                MaterialTheme.colors.onSurface,
                MaterialTheme.shapes.large
            )
            .clickable {
                onClick.invoke()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )
    }
}


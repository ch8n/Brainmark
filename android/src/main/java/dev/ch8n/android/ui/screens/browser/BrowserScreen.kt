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
import dev.ch8n.android.utils.toast
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.controllers.BrowserController
import dev.ch8n.common.utils.AndroidPreview


@Composable
fun PreviewBrowserScreen(
    componentContext: DefaultComponentContext
) {
    val context = LocalContext.current
    val controller = remember {
        BrowserController(
            componentContext = componentContext,
            navigateTo = {
                "On navigate to ${it::class.simpleName}".toast(context)
            },
            onBack = {
                "On Back".toast(context)
            }
        )
    }
    AndroidPreview(
        isSplitView = false,
        isDark = true,
    ) {
        BrowserScreen(controller)
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(
    controller: BrowserController,
) {

    val screenState by controller.screenState.collectAsState()

    LaunchedEffect(screenState) {
        val bookmark = screenState.bookmark
        if (bookmark.id == "0") {
            controller.getBookmark("")
        }
    }

    val (isLoading, setLoading) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // TODO make collapsable
        ToolbarBrowser(
            bookmark = screenState.bookmark,
            tags = screenState.tags,
            modifier = Modifier
                .fillMaxWidth(),
            onBackPressed = controller.onBack,
            onShowFlashCards = {},
            onEditBookmark = {},
            onArchiveBookmark = {},
            onClickedTag = {}
        )
    }

}


@Composable
private fun ToolbarBrowser(
    bookmark: Bookmark,
    tags: List<Tags>,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onShowFlashCards: () -> Unit,
    onEditBookmark: (bookmark: Bookmark) -> Unit,
    onArchiveBookmark: (bookmark: Bookmark) -> Unit,
    onClickedTag: (tag: Tags) -> Unit
) {

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .alpha(0.4f)
        ) {
            AsyncImage(
                model = bookmark.mainImage,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }

        AsyncImage(
            model = R.drawable.back,
            modifier = Modifier
                .padding(24.dp)
                .size(24.dp)
                .align(Alignment.TopStart)
                .clickable { onBackPressed.invoke() },
            contentDescription = "",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = Color.White
            )
        )

        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 74.dp, end = 24.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = bookmark.title,
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
                    .heightIn(min = 300.dp, max = 300.dp)
                    .border(1.dp, Color.Red)
                    .verticalScroll(rememberScrollState())
            ) {
                tags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth()
                            .height(35.dp),
                        onTagClicked = onClickedTag
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(36.dp)
            ) {
                AsyncImage(
                    model = R.drawable.flash_card,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onShowFlashCards.invoke() },
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = Color.White
                    )
                )

                AsyncImage(
                    model = R.drawable.edit,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onEditBookmark.invoke(bookmark) },
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = Color.White
                    )
                )

                AsyncImage(
                    model = R.drawable.archive,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onArchiveBookmark.invoke(bookmark) },
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = Color.White
                    )
                )
            }
        }

    }

}
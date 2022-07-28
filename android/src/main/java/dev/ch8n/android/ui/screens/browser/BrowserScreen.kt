package dev.ch8n.android.ui.screens.browser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import dev.ch8n.android.R
import dev.ch8n.android.design.components.TagChip
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.controllers.BrowserController
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewBrowserScreen(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        BrowserController(
            componentContext = componentContext,
            navigateTo = {},
            onBack = {}
        )
    }
    DevelopmentPreview { isDark ->
        BrowserScreen(controller)
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(
    controller: BrowserController,
) {

    val tags by controller.tags.collectAsState()
    val bookmark by controller.bookmark.collectAsState()
    val (isLoading, setLoading) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ToolbarBrowser(
            bookmark = bookmark,
            tags = tags,
            modifier = Modifier.fillMaxWidth(),
            onBackPressed = controller.onBack,
            onShowFlashCards = {},
            onEditBookmark = {},
            onMarkedRead = {}
        )

        val webViewClient = remember {
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    setLoading.invoke(true)
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    setLoading.invoke(false)
                }
            }
        }

        AndroidView(
            factory = ::WebView,
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = isLoading,
                    color = MaterialTheme.colors.surface,
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = MaterialTheme.colors.onSurface,
                        animationSpec = InfiniteRepeatableSpec(
                            animation = tween(durationMillis = 400)
                        )
                    )
                )
                .verticalScroll(rememberScrollState()),
            update = {
                it.webViewClient = webViewClient
                it.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                with(it.settings) {
                    loadsImagesAutomatically = true
                    javaScriptEnabled = true
                }
                it.loadUrl(bookmark.bookmarkUrl)
            }
        )

    }

}


@Composable
fun CreateTag(
    modifier: Modifier,
    selectedTag: Tags,
    updatedSelectedTag: (updatedTag: Tags) -> Unit,
    saveTag: (updatedTag: Tags) -> Unit,
    onDeleteTag: (tag: Tags) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            value = selectedTag.name,
            onValueChange = {
                updatedSelectedTag.invoke(selectedTag.copy(name = it))
            },
            shape = MaterialTheme.shapes.large,
            singleLine = true,
            label = {
                Text("Tag Name")
            },
            textStyle = MaterialTheme.typography.subtitle1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface
            )
        )

        Row(
            modifier = Modifier.width(240.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(R.drawable.color_picker),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = {
                            // TODO update color
                            updatedSelectedTag.invoke(selectedTag)
                        }
                    ),
                tint = MaterialTheme.colors.onSurface,
            )

            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = {
                            onDeleteTag.invoke(selectedTag)
                        }
                    ),
                tint = MaterialTheme.colors.onSurface,
            )

            Icon(
                painter = painterResource(R.drawable.save),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = {
                            saveTag.invoke(selectedTag)
                        }
                    ),
                tint = MaterialTheme.colors.onSurface,
            )
        }
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
    onMarkedRead: (bookmark: Bookmark) -> Unit
) {


    Box(
        modifier = modifier.size(336.dp)
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
                    .align(Alignment.TopStart)
                    .clickable { onBackPressed.invoke() },
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
                    .height(164.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                tags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth()
                            .height(35.dp),
                        onTagClicked = {

                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .align(Alignment.BottomEnd),
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
                    .clickable { onMarkedRead.invoke(bookmark) },
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Color.White
                )
            )
        }

    }


}
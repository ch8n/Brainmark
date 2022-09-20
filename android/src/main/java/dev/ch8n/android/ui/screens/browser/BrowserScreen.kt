package dev.ch8n.android.ui.screens.browser

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
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
import dev.ch8n.common.ui.controllers.PreviewBookmarkController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.AndroidPreview


@Composable
fun PreviewBrowserScreen(
    componentContext: DefaultComponentContext
) {
    val context = LocalContext.current
    val controller = remember {
        PreviewBookmarkController(
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
    controller: PreviewBookmarkController,
) {

    val context = LocalContext.current
    val screenState by controller.screenState.collectAsState()

    LaunchedEffect(screenState) {
        val bookmark = screenState.bookmark
        if (bookmark.id == "0") {
            controller.getBookmark("")
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
                    .clickable { controller.onBack.invoke() },
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
                        .clickable {
                            "show Notes".toast(context)
                        },
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
                        .clickable {
                            "on edit bookmark".toast(context)
                        },
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
                        .clickable {
                            "archive bookmark".toast(context)
                        },
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = Color.White
                    )
                )
            }

            OutlinedButton(
                onClick = {
                    controller.navigateTo(
                        Destinations.ReaderScreen(screenState.bookmark.bookmarkUrl)
                    )
                }
            ) {
                Text("Preview in Reader Mode")
            }

            OutlinedButton(
                onClick = {
                    context.launchChromeTab(
                        screenState.bookmark.bookmarkUrl
                    )
                }
            ) {
                Text("View in Chrome")
            }

            OutlinedButton(
                onClick = {
                    "open webview".toast(context)
                }
            ) {
                Text("View in App | bypass paywalls.")
            }
        }
    }


}


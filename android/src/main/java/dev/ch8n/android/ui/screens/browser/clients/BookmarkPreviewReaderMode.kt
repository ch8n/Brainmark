package dev.ch8n.android.ui.screens.browser.clients

import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import dev.ch8n.android.R
import dev.ch8n.android.ui.components.ScrollableColumn
import dev.ch8n.android.utils.rememberMutableState
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.remote.services.readerview.ReaderViewDTO
import dev.ch8n.common.ui.controllers.PreviewReaderModeController
import dev.ch8n.common.ui.navigation.NavController

@Composable
fun HtmlText(
    html: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = {
            it.textSize = 12f
            it.setTextColor(color.toArgb())
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}


class AndroidPreviewReaderModeController(
    navController: NavController,
    bookmark: Bookmark,
) : PreviewReaderModeController(navController, bookmark) {
    @Composable
    override fun Render() {
        ReaderScreen(this)
    }
}

// TODO feature
// 1. select and highlight
// 2. drop notes
// 3. beautify
// 4. can we use type???
@Composable
fun ReaderScreen(
    controller: PreviewReaderModeController
) {

    val reader by controller.reader.collectAsState()
    var isPlainText by rememberMutableState(false)

    LaunchedEffect(reader) {
        if (reader == ReaderViewDTO.empty) {
            controller.refreshReader()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {

        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = reader.title,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onSurface
            )

            Text(
                text = reader.byline,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onSurface
            )

            AnimatedVisibility(isPlainText) {
                Text(
                    text = reader.plainText,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface
                )
            }

            AnimatedVisibility(!isPlainText) {
                HtmlText(
                    html = reader.htmlText,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Spacer(Modifier.size(100.dp))
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = {
                isPlainText = !isPlainText
            }
        ) {
            Icon(
                painter = if (!isPlainText) {
                    painterResource(R.drawable.text)
                } else {
                    painterResource(R.drawable.internet)
                },
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}
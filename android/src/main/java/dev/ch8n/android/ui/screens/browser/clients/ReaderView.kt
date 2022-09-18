package dev.ch8n.android.ui.screens.browser.clients

import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import dev.ch8n.android.ui.components.ScrollableColumn
import dev.ch8n.android.utils.rememberMutableState
import dev.ch8n.common.data.remote.services.readerview.ReaderViewDTO
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.domain.di.DomainResolver
import dev.ch8n.common.domain.di.provideReaderView
import kotlinx.coroutines.launch

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}


// TODO wip reader view
@Composable
fun ReaderView() {

    val readerParser = remember {
        val htmlParserService = DomainInjector.htmlParserService
        DomainResolver.provideReaderView(htmlParserService)
    }

    var reader by rememberMutableState(ReaderViewDTO.empty)
    var url by rememberMutableState("")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ScrollableColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        OutlinedTextField(
            value = url,
            onValueChange = { url = it }
        )

        Button(onClick = {
            scope.launch {
                reader = readerParser.getReaderViewContent(url)
                context.launchChromeTab(url)
            }
        }) {
            Text("Parse")
        }

        Text(
            text = reader.title,
            fontSize = 24.sp,
        )

        Divider(modifier = Modifier.fillMaxWidth())

        Text(
            text = reader.byline,
            fontSize = 24.sp,
        )

        Divider(modifier = Modifier.fillMaxWidth())

        Text(
            text = reader.excerpt,
            fontSize = 24.sp,
        )

        Divider(modifier = Modifier.fillMaxWidth())

        Column(Modifier.fillMaxWidth().border(1.dp, Color.Cyan)) {
            reader.plainText.split(". ").forEach {
                Text(
                    text = "$it.",
                    fontSize = 16.sp,
                )

                Divider(modifier = Modifier.fillMaxWidth(), Color.Black, 2.dp)
            }
        }


        HtmlText(
            html = reader.htmlText,
            modifier = Modifier.fillMaxSize()
        )

        Divider(modifier = Modifier.fillMaxWidth())
    }

}
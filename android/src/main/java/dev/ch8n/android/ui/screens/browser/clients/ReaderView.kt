package dev.ch8n.android.ui.screens.browser.clients

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import dev.ch8n.android.ui.components.ScrollableColumn
import dev.ch8n.android.utils.rememberMutableState
import dev.ch8n.common.data.remote.services.readerview.ReaderViewDTO
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.domain.di.DomainResolver
import dev.ch8n.common.domain.di.provideReaderView

// TODO wip reader view
@Composable
fun ReaderView(url: String) {
    val readerParser = remember {
        val htmlParserService = DomainInjector.htmlParserService
        DomainResolver.provideReaderView(htmlParserService)
    }

    var reader by rememberMutableState(ReaderViewDTO.empty)

    LaunchedEffect(readerParser) {
        reader = readerParser.getReaderViewContent(url)
    }

    ScrollableColumn {
        Text(
            text = reader.toString(),
            fontSize = 24.sp,
        )
    }

}
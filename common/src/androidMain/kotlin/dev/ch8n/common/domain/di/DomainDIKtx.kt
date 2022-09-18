package dev.ch8n.common.domain.di

import dev.ch8n.common.data.remote.services.htmlParser.HtmlParserService
import dev.ch8n.common.data.remote.services.readerview.ReaderViewService


fun DomainResolver.provideReaderView(htmlParser: HtmlParserService): ReaderViewService {
    return ReaderViewService(htmlParser)
}

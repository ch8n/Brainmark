package dev.ch8n.common.data.remote.services.readerview

import dev.ch8n.common.data.remote.services.htmlParser.HtmlParserService
import kotlinx.coroutines.Dispatchers
import net.dankito.readability4j.Article
import net.dankito.readability4j.Readability4J

actual class ReaderViewService(
    private val htmlParserService: HtmlParserService
) {
    actual suspend fun getReaderViewContent(url: String): ReaderViewDTO = with(Dispatchers.IO) {
        val html = htmlParserService.getHtml(url)
        val readability4J = Readability4J(url, html)
        val article: Article = readability4J.parse()
        val textContent: String = article.textContent ?: ""
        val htmlContent = article.content ?: ""
        val title: String = article.title ?: ""
        val byline: String = article.byline ?: ""
        val excerpt: String = article.excerpt ?: ""
        return ReaderViewDTO(
            title = title,
            byline = byline,
            excerpt = excerpt,
            plainText = textContent,
            htmlText = htmlContent
        )
    }
}
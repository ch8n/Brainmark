package dev.ch8n.common.data.remote.services.htmlParser

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HtmlParserService(
    private val httpClient: HttpClient
) {
    suspend fun getHtml(url: String): String = withContext(Dispatchers.IO) {
        httpClient.get(url).bodyAsText()
    }

}
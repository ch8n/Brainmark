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

    fun parseMeta(html: String): MetaDTO {
        val htmlTags = html.splitToSequence("<", "/>")
        val metaTags = htmlTags.filter { it.contains("meta") }

        // title
        val title = metaTags.metaSelectorName("og:title")
            .ifEmpty { metaTags.metaSelectorProperty("og:title") }
            .ifEmpty { metaTags.metaSelectorName("title") }
            .ifEmpty { metaTags.metaSelectorProperty("title") }
            .ifEmpty { htmlTags.getHtmlTagContent("title") }

        // description
        val description = metaTags.metaSelectorName("og:description")
            .ifEmpty { metaTags.metaSelectorProperty("og:description") }
            .ifEmpty { metaTags.metaSelectorName("description") }
            .ifEmpty { metaTags.metaSelectorProperty("description") }

        // mediaType
        val mediaType = metaTags.metaSelectorName("og:type")
            .ifEmpty { metaTags.metaSelectorProperty("og:type") }
            .ifEmpty { metaTags.metaSelectorName("og:medium") }
            .ifEmpty { metaTags.metaSelectorProperty("og:medium") }

        // mainImage
        val mainImage = metaTags.metaSelectorName("og:image")
            .ifEmpty { metaTags.metaSelectorProperty("og:image") }
            .ifEmpty { htmlTags.metaSelectorName("image:src") }
            .ifEmpty { htmlTags.metaSelectorProperty("image:src") }

        // favIcon
        val favIcon = htmlTags.getHref("apple-touch-icon")
            .ifEmpty { htmlTags.getHref("icon") }

        // author or site
        val authorOrSite = metaTags.metaSelectorName("author")
            .ifEmpty { metaTags.metaSelectorProperty("author") }
            .ifEmpty { metaTags.metaSelectorName("og:site_name") }
            .ifEmpty { metaTags.metaSelectorProperty("og:site_name") }
            .ifEmpty { metaTags.metaSelectorName("og:url") }
            .ifEmpty { metaTags.metaSelectorProperty("og:url") }

        return MetaDTO(
            title = title,
            description = description,
            mediaType = mediaType,
            mainImage = mainImage,
            favIcon = favIcon,
            authorOrSite = authorOrSite
        )
    }

}

inline fun Sequence<String>.getHref(rel: String): String {
    return this
        .filter { it.contains("rel=\"$rel\"") }
        .map { it.split("href=\"", "\"").getOrNull(7) ?: "" }
        .firstOrNull { it.contains("http") } ?: ""
}


inline fun Sequence<String>.getHtmlTagContent(tagName: String): String {
    return this
        .filter { it.contains("$tagName>") }
        .map {
            it.split("$tagName>", "</$tagName>")
                .getOrNull(1) ?: ""
        }
        .firstOrNull() ?: ""
}

inline fun Sequence<String>.metaSelectorName(nameAttr: String): String {
    return this
        .filter { it.contains("name=\"$nameAttr\"", ignoreCase = true) }
        .map { it.split("content=").getOrNull(1) ?: "" }
        .firstOrNull() ?: ""
}

inline fun Sequence<String>.metaSelectorProperty(propertyAttr: String): String {
    return this
        .filter { it.contains("property=\"$propertyAttr\"", ignoreCase = true) }
        .map { it.split("content=").getOrNull(1) ?: "" }
        .firstOrNull() ?: ""
}
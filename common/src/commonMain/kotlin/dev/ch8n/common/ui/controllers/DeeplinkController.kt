package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.CreateBookmarksDestination
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.ui.navigation.PreviewBookmarkHomeDestination
import kotlinx.coroutines.flow.first

object DeeplinkController {

    private val getBookmarkByUrl = DomainInjector
        .bookmarkUseCase
        .getBookmarkByUrl

    suspend fun handleDeeplink(url: String): Destinations? {
        return kotlin.runCatching {
            val bookmark = getBookmarkByUrl.invoke(url).first()
            when {
                bookmark != Bookmark.Empty -> PreviewBookmarkHomeDestination(bookmark)
                else -> CreateBookmarksDestination(url)
            }
        }.getOrNull()
    }
}
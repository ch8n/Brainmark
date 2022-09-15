package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.*

class BookmarkScreenController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val navigateBack: () -> Unit,
) : DecomposeController(componentContext) {

    private val bookmarkPager = DomainInjector
        .bookmarkUseCase
        .getBookmarksPaging

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()
    fun loadFirstBookmark() {
        nextBookmark()
    }

    fun nextBookmark() {
        val current = _bookmarks.value
        val limit = 5L
        val offset = current.size.toLong()
        bookmarkPager.invoke(limit, offset)
            .onEach { nextBookmarks ->
                val updated = current + nextBookmarks
                _bookmarks.update { updated }
            }
            .launchIn(this)
    }

}
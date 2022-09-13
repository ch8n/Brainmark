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

    fun nextBookmark(createdAt: Long) {
        bookmarkPager.invoke(createdAt)
            .onEach { nextBookmarks ->
                val updated = _bookmarks.value + nextBookmarks
                _bookmarks.update { updated }
            }
            .launchIn(this)
    }

}
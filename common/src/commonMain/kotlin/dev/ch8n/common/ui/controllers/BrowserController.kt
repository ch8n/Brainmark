package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class BrowserController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    private fun withLoading(action: suspend () -> Unit) {
        try {
            _screenState.update { it.copy(isLoading = true) }
            launch { action.invoke() }
        } finally {
            _screenState.update { it.copy(isLoading = false) }
        }
    }

    fun getBookmark(bookmarkId: String) {
        // todo fix use get bookmark by id
        withLoading {
            getBookmarkById(1, 1)
                .flatMapMerge { bookmarks ->
                    val bookmark = bookmarks.first()
                    val tagIds = bookmark.tagIds
                    _screenState.update {
                        it.copy(bookmark = bookmarks.first())
                    }
                    getTagsById.invoke(tagIds)
                }
                .onEach { tags ->
                    _screenState.update {
                        it.copy(tags = tags)
                    }
                }
                .onceIn(this)
        }
    }

    private val getBookmarkById = DomainInjector
        .bookmarkUseCase
        // TODO change with get bookmark with id
        .getAllBookmarksPaging

    private val getTagsById = DomainInjector
        .tagUseCase
        .getTagsByIdsUseCase

    @Stable
    data class ScreenState(
        val isLoading: Boolean,
        val isError: Boolean,
        val errorMsg: String,
        val bookmark: Bookmark,
        val tags: List<Tags>,
    ) {
        companion object {
            fun reset() = ScreenState(
                isLoading = false,
                isError = false,
                errorMsg = "",
                bookmark = Bookmark.Empty,
                tags = emptyList()
            )
        }
    }

    private val _screenState = MutableStateFlow(ScreenState.reset())
    val screenState = _screenState.asStateFlow()


}
package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

abstract class BookmarksController(
    navController: NavController
) : UiController(navController) {

    @Stable
    data class ScreenState(
        val selectedTag: Tags,
        val isLoading: Boolean,
        val isError: Boolean,
        val errorMsg: String,
        val searchQuery: String,
    ) {
        companion object {

            val allTagOption = Tags(
                id = "1",
                name = "All",
                color = 0,
            )

            val unTaggedOption = Tags(
                id = "2",
                name = "Untagged",
                color = 0,
            )

            fun reset() = ScreenState(
                selectedTag = allTagOption,
                isLoading = false,
                isError = false,
                errorMsg = "",
                searchQuery = "",
            )
        }
    }

    private val _screenState = MutableStateFlow(ScreenState.reset())
    val screenState = _screenState.asStateFlow()

    private val getAllTags = DomainInjector
        .tagUseCase
        .getAllTags

    private val allBookmarkPager = DomainInjector
        .bookmarkUseCase
        .getAllBookmarksPaging

    private val bookmarkByTagPager = DomainInjector
        .bookmarkUseCase
        .getBookmarksByTagPaging

    private val searchBookmarkByTagPager = DomainInjector
        .bookmarkUseCase
        .searchBookmarkByTagPaging

    private val searchAllBookmarkPager = DomainInjector
        .bookmarkUseCase
        .searchAllBookmarkPaging

    private val searchUntaggedBookmarkPager = DomainInjector
        .bookmarkUseCase
        .searchUntaggedBookmarkPaging

    private val untaggedBookmarks = DomainInjector
        .bookmarkUseCase
        .getUntaggedBookmarks

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()

    private val _pagingInvalidateKey = MutableStateFlow(uuid4().toString())
    val pagingInvalidateKey = _pagingInvalidateKey.asStateFlow()

    private val _tags = MutableStateFlow<List<Tags>>(emptyList())
    val tags = _tags.asStateFlow()

    fun nextTags() {
        val current = _tags.value
        val limit = 5L
        val offset = current.size.toLong()
        getAllTags.invoke(limit, offset)
            .onEach { nextTags ->
                val updated = current + nextTags
                _tags.update { updated }
            }.onceIn(this)
    }

    fun nextBookmark() {
        val current = _bookmarks.value
        val limit = 5L
        val offset = current.size.toLong()
        val selectedTag = _screenState.value.selectedTag
        val searchQuery = _screenState.value.searchQuery.trim()
        val flow = when {
            searchQuery.isNotEmpty() -> when (selectedTag) {
                ScreenState.allTagOption -> searchAllBookmarkPager.invoke(
                    searchQuery,
                    limit,
                    offset
                )
                ScreenState.unTaggedOption -> searchUntaggedBookmarkPager.invoke(
                    searchQuery,
                    limit,
                    offset
                )
                else -> searchBookmarkByTagPager.invoke(searchQuery, selectedTag.id, limit, offset)
            }

            else -> when (selectedTag) {
                ScreenState.allTagOption -> allBookmarkPager.invoke(limit, offset)
                ScreenState.unTaggedOption -> untaggedBookmarks.invoke(limit, offset)
                else -> bookmarkByTagPager.invoke(selectedTag.id, limit, offset)
            }
        }

        flow.onEach { nextBookmarks ->
            val updated = current + nextBookmarks
            _bookmarks.update { updated }
        }.onceIn(this)
    }

    fun allTagSelected() {
        onTagSelected(ScreenState.allTagOption)
    }

    fun invalidatePaging() {
        _bookmarks.value = emptyList()
        _pagingInvalidateKey.value = uuid4().toString()
    }

    fun onTagSelected(tag: Tags) {
        _screenState.update {
            it.copy(selectedTag = tag)
        }
        invalidatePaging()
    }

    fun onSearchQueryUpdated(query: String) {
        _screenState.update {
            it.copy(searchQuery = query)
        }
        invalidatePaging()
    }

    fun onClearSearchQuery() {
        _screenState.update {
            it.copy(searchQuery = "")
        }
        invalidatePaging()
    }

    fun onUntaggedSelected() {
        onTagSelected(ScreenState.unTaggedOption)
    }

}
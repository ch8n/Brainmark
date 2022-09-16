package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class BookmarkScreenController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val navigateBack: () -> Unit,
) : DecomposeController(componentContext) {

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

    val allTags = DomainInjector
        .tagUseCase
        .getAllTagsUseCase()

    private val allBookmarkPager = DomainInjector
        .bookmarkUseCase
        .getAllBookmarksPaging

    private val bookmarkByTagPager = DomainInjector
        .bookmarkUseCase
        .getBookmarksByTagPaging

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks = _bookmarks.asStateFlow()

    private val _pagingInvalidateKey = MutableStateFlow(uuid4().toString())
    val pagingInvalidateKey = _pagingInvalidateKey.asStateFlow()

    fun nextBookmark() {
        val current = _bookmarks.value
        val limit = 5L
        val offset = current.size.toLong()
        val selectedTag = _screenState.value.selectedTag
        val flow = if (selectedTag == ScreenState.allTagOption) {
            allBookmarkPager.invoke(limit, offset)
        } else {
            bookmarkByTagPager.invoke(selectedTag.id, limit, offset)
        }
        flow.onEach { nextBookmarks ->
            val updated = current + nextBookmarks
            _bookmarks.update { updated }
        }.onceIn(this)
    }

    fun allTagSelected() {
        onTagSelected(ScreenState.allTagOption)
    }

    fun onTagSelected(tag: Tags) {
        _screenState.update {
            it.copy(selectedTag = tag)
        }
        _bookmarks.value = emptyList()
        _pagingInvalidateKey.value = uuid4().toString()
    }

}
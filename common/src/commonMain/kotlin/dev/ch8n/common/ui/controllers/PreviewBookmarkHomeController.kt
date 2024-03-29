package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.HomeDestination
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.concurrent.CancellationException

@OptIn(FlowPreview::class)
abstract class PreviewBookmarkHomeController(
    navController: NavController,
    private val bookmark: Bookmark
) : UiController(navController) {

    private fun withLoading(action: suspend () -> Unit) {
        try {
            _screenState.update { it.copy(isLoading = true) }
            launch { action.invoke() }
        } catch (e: Exception) {
            if (e !is CancellationException) {
                _screenState.update {
                    it.copy(
                        isError = true,
                        errorMsg = e.localizedMessage ?: "Something went wrong!"
                    )
                }
            }
        } finally {
            _screenState.update { it.copy(isLoading = false) }
        }
    }

    private val updateBookmarkReadAt = DomainInjector
        .bookmarkUseCase
        .upsertBookmark

    private val updateBookmarkArchived = DomainInjector
        .bookmarkUseCase
        .upsertBookmark

    private val deleteBookmark = DomainInjector
        .bookmarkUseCase
        .deleteBookmark


    fun archiveBookmark() {
        val currentBookmark = _screenState.value.bookmark
        val updated = currentBookmark.copy(isArchived = !currentBookmark.isArchived)
        updateBookmarkArchived
            .invoke(updated)
            .onEach {
                _screenState.update {
                    it.copy(bookmark = updated)
                }
            }
            .onceIn(this)
    }

    private fun updateBookmarkReadTime(bookmark: Bookmark) {
        launch {
            updateBookmarkReadAt.invoke(
                bookmark.copy(
                    lastReadAt = Clock.System.now().epochSeconds
                )
            ).onceIn(this)
        }
    }

    fun getBookmark() {
        withLoading {
            updateBookmarkReadTime(bookmark)
            val tagIds = bookmark.tagIds
            _screenState.update {
                it.copy(bookmark = bookmark)
            }
            getTagsById.invoke(tagIds)
                .onEach { tags ->
                    _screenState.update {
                        it.copy(tags = tags)
                    }
                }
                .onceIn(this)
        }
    }

    fun onBookmarkDelete() {
        deleteBookmark
            .invoke(bookmark.id)
            .onCompletion { backTo(HomeDestination) }
            .onceIn(this)
    }

    private val getTagsById = DomainInjector
        .tagUseCase
        .getTagsByIds

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
package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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
        .upsertBookmarkUseCase

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
            getBookmarkById(bookmark.id)
                .flatMapMerge { bookmark ->
                    updateBookmarkReadTime(bookmark)
                    val tagIds = bookmark.tagIds
                    _screenState.update {
                        it.copy(bookmark = bookmark)
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
        .getBookmarkByIdUseCase

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
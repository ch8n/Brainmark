package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.FlashCard
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach

abstract class HomeController(
    navController: NavController
) : UiController(navController) {

    private val _lastReadBookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val lastReadBookmarks = _lastReadBookmarks.asStateFlow()

    private val getLastRead = DomainInjector
        .bookmarkUseCase
        .getBookmarksByLastReadPaging

    fun getLastReadBookmarks() {
        getLastRead.invoke(5, 0)
            .onEach {
                _lastReadBookmarks.emit(it)
            }
            .onceIn(this)
    }


    private val _readingRecommendations = MutableStateFlow<List<Bookmark>>(emptyList())
    val readingRecommendations = _readingRecommendations.asStateFlow()

    private val getReadingRecommendations = DomainInjector
        .bookmarkUseCase
        .getReadingRecommendations

    fun getReadingRecommendations() {
        getReadingRecommendations.invoke()
            .onEach {
                _readingRecommendations.emit(it)
            }
            .onceIn(this)
    }


    private val bookmarkSample = listOf(
        Bookmark.SAMPLE,
        Bookmark.SAMPLE,
        Bookmark.SAMPLE,
        Bookmark.SAMPLE,
        Bookmark.SAMPLE,
    )

    // TODO create UI model
    val flashCardSample = listOf(
        FlashCard.SAMPLE,
        FlashCard.SAMPLE,
        FlashCard.SAMPLE
    )

    private val _bookmarks = MutableStateFlow(bookmarkSample)
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow()

    fun setBookmarkReviewed(bookmark: Bookmark, isReviewed: Boolean) {
        changeState {
            this.map {
                if (it.id == bookmark.id) {
                    it.copy(isArchived = isReviewed)
                } else {
                    it
                }
            }
        }
    }

    private inline fun changeState(reducer: List<Bookmark>.() -> List<Bookmark>): List<Bookmark> {
        val updatedBookmarks = bookmarks.value.reducer()
        _bookmarks.value = updatedBookmarks
        return updatedBookmarks
    }
}
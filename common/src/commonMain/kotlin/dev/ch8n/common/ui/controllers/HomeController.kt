package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class HomeController(
    navController: NavController
) : UiController(navController) {

    private val _lastReadBookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val lastReadBookmarks = _lastReadBookmarks.asStateFlow()

    val isLoading = MutableStateFlow(false)

    fun onSwipeRefresh() {
        launch {
            isLoading.update { true }
            try {
                _lastReadBookmarks.update { emptyList() }
                _revisionRecommendations.update { emptyList() }
                _readingRecommendations.update { emptyList() }
                getLastReadBookmarks()
                getRevisionBookmarks()
                getReadingRecommendations()
            } finally {
                isLoading.update { false }
            }
        }
    }

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

    private val _revisionRecommendations = MutableStateFlow<List<Bookmark>>(emptyList())
    val revisionRecommendations = _revisionRecommendations.asStateFlow()

    private val getRevisions = DomainInjector
        .bookmarkUseCase
        .getRevisionRecommendations

    fun getRevisionBookmarks() {
        getRevisions.invoke()
            .onEach {
                _revisionRecommendations.emit(it)
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
}
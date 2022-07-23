package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    private val _bookmarks = MutableStateFlow(listOf(Bookmark.SAMPLE))
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
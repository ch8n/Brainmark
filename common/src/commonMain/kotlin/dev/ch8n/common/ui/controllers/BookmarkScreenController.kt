package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Tags
import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookmarkScreenController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val navigateBack: () -> Unit,
) : DecomposeController(componentContext) {

    private val createBookmarkUseCase = DomainInjector.bookmarkUseCase.createBookmarkUseCase

    private val _bookmark = MutableStateFlow(Bookmark.SAMPLE)
    val bookmark: StateFlow<Bookmark> = _bookmark.asStateFlow()

    private inline fun changeState(reducer: Bookmark.() -> Bookmark): Bookmark {
        val updatedTag = bookmark.value.reducer()
        _bookmark.value = updatedTag
        return updatedTag
    }

    fun onChangeBookmarkUrl(url: String) {
        changeState {
            copy(bookmarkUrl = url)
        }
    }

    fun onTagUpdated(tag: Tags) {
//        val tags = bookmark.value.run {
//            val list = mutableListOf(primaryTagId)
//            list.addAll(secondaryTagIds)
//            list
//        }
//        changeState {
//            copy(secondaryTagIds = tags + tag.id)
//        }
    }

    fun onChangeReminderTime() {
        changeState {
            copy(remindAt = 0L)
        }
    }

    fun onChangeNotes(notes: String) {
//        changeState {
//            copy(notes = notes)
//        }
    }

    fun onChangeReviewed(isReviewed: Boolean) {
        changeState {
            copy(isArchived = isReviewed)
        }
    }

    fun onCreateBookmark() = with(bookmark.value) {
//        createBookmarkUseCase(
//            url = url,
//            createdAt = createdAt,
//            remindAt = remindAt,
//            isReviewed = isArchived,
//            //TODO fix tags
//            tagIds = secondaryTagIds,
//            notes = notes
//        )
    }
}
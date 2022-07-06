package dev.ch8n.common.ui.bookmark

import dev.ch8n.common.data.model.Bookmark

interface BookmarkPresenter {
    fun createBookmark(bookmark: Bookmark)
    fun removeBookmark(bookmark: Bookmark)
    fun editBookmark(bookmark: Bookmark)
}
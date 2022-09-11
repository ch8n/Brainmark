package dev.ch8n.common.domain.usecases

import dev.ch8n.common.data.local.database.sources.BookmarkDataSource
import dev.ch8n.common.data.model.Bookmark
import kotlinx.coroutines.flow.flow

class BookmarkUseCases(
    val getAllBookmarkUseCase: GetAllBookmarkUseCase,
    val getBookmarkByIdUseCase: GetBookmarkByIdUseCase,
    val createBookmarkUseCase: CreateBookmarkUseCase,
    val updateBookmarkUseCase: UpdateBookmarkUseCase,
    val deleteBookmarkUseCase: DeleteBookmarkUseCase
)

class GetBookmarksByIdsUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(bookmarkIds: List<String>) = flow {
        val bookmarks = bookmarksDataSource.getBookmarksByIds(bookmarkIds)
        emit(bookmarks)
    }
}


class GetAllBookmarkUseCase(
    private val bookmarkDataSource: BookmarkDataSource
) {
    operator fun invoke() = bookmarkDataSource.getAllBookmarks()
}


class GetBookmarkByIdUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(id: String) = flow {
        val bookmark = bookmarksDataSource.getBookmarkById(id = id) ?: error("Bookmark not found")
        emit(bookmark)
    }
}

class UpdateBookmarkUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(bookmark: Bookmark) = flow {
        val updatedId = bookmarksDataSource.updateBookmark(bookmark)
        emit(updatedId)
    }
}

class CreateBookmarkUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(bookmark: Bookmark) = flow {
        val id = bookmarksDataSource.createBookmark(bookmark)
        emit(id)
    }
}

class DeleteBookmarkUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(id: String) = flow {
        bookmarksDataSource.deleteBookmark(id)
        emit(Unit)
    }
}
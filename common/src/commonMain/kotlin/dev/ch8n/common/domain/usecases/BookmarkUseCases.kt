package dev.ch8n.common.domain.usecases

import dev.ch8n.common.data.local.database.sources.BookmarkDataSource
import dev.ch8n.common.data.model.Bookmark
import kotlinx.coroutines.flow.flow

class BookmarkUseCases(
    val getBookmarkByUrl: GetBookmarkByUrlUseCase,
    val getBookmarkByIdUseCase: GetBookmarkByIdUseCase,
    val upsertBookmarkUseCase: UpsertBookmarkUseCase,
    val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    val getAllBookmarksPaging: GetBookmarksPaging,
    val getBookmarksByTagPaging: GetBookmarkByTagPaging,
    val searchAllBookmarkPaging: SearchAllBookmarkPaging,
    val searchUntaggedBookmarkPaging: SearchUntaggedBookmarkPaging,
    val searchBookmarkByTagPaging: SearchBookmarkByTagPaging,
    val getBookmarksByLastReadPaging: GetBookmarksByLastReadPaging,
    val getReadingRecommendations: GetReadingRecommendations,
    val getRevisionBookmarks: GetRevisionBookmarks,
    val getUntaggedBookmarks: GetUntaggedBookmarkPaging,
)

class GetBookmarkByUrlUseCase(
    private val bookmarkDataSource: BookmarkDataSource
) {
    operator fun invoke(url: String) = flow {
        val bookmark = bookmarkDataSource.getBookmarkByUrl(url)
        emit(bookmark)
    }
}

class GetBookmarkByIdUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(id: String) = flow {
        val bookmark = bookmarksDataSource.getBookmarkById(id = id) ?: error("Bookmark not found")
        emit(bookmark)
    }
}

class UpsertBookmarkUseCase(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(bookmark: Bookmark) = flow {
        val id = bookmarksDataSource.upsertBookmark(bookmark)
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


class GetBookmarksPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.getAllBookmarksPaging(limit, offset)
        emit(bookmarks)
    }
}

class GetBookmarksByLastReadPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.getBookmarksByLastReadPaging(limit, offset)
        emit(bookmarks)
    }
}


class GetRevisionBookmarks(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke() = flow {
        val bookmarks = bookmarksDataSource.getRevisionRecommendations()
        emit(bookmarks)
    }
}

class GetReadingRecommendations(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke() = flow {
        val bookmarks = bookmarksDataSource.getReadingRecommendations()
        emit(bookmarks)
    }
}

class GetUntaggedBookmarkPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.untaggedBookmarksPaging(limit, offset)
        emit(bookmarks)
    }
}

class SearchUntaggedBookmarkPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(query: String, limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.searchBookmarksByTagPaging(query, "", limit, offset)
        emit(bookmarks)
    }
}

class GetBookmarkByTagPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(tagId: String, limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.getBookmarksByTagPaging(tagId, limit, offset)
        emit(bookmarks)
    }
}

class SearchBookmarkByTagPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(keyword: String, tagId: String, limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.searchBookmarksByTagPaging(keyword, tagId, limit, offset)
        emit(bookmarks)
    }
}

class SearchAllBookmarkPaging(
    private val bookmarksDataSource: BookmarkDataSource
) {
    operator fun invoke(keyword: String, limit: Long, offset: Long) = flow {
        val bookmarks = bookmarksDataSource.searchAllBookmarksPaging(keyword, limit, offset)
        emit(bookmarks)
    }
}




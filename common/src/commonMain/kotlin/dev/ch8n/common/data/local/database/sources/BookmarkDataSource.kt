package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.Query
import dev.ch8n.common.BookmarkEntity
import dev.ch8n.common.SearchAllBookmarkPaging
import dev.ch8n.common.SearchBookmarkByTagPaging
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface BookmarkDataSource {
    suspend fun getReadingRecommendations(): List<Bookmark>
    suspend fun getRevisionRecommendations(): List<Bookmark>
    suspend fun getBookmarksByLastReadPaging(limit: Long, offset: Long): List<Bookmark>

    suspend fun getAllBookmarksPaging(limit: Long, offset: Long): List<Bookmark>
    suspend fun getBookmarksByTagPaging(tagId: String, limit: Long, offset: Long): List<Bookmark>
    suspend fun searchAllBookmarksPaging(keyword: String, limit: Long, offset: Long): List<Bookmark>
    suspend fun searchBookmarksByTagPaging(
        keyword: String,
        tagId: String,
        limit: Long,
        offset: Long
    ): List<Bookmark>

    suspend fun getBookmarkOrDefault(id: String, default: () -> Bookmark): Bookmark

    suspend fun upsertBookmark(bookmark: Bookmark): String
    suspend fun getBookmarkByUrlOrDefault(url: String, default: () -> Bookmark): Bookmark
    suspend fun deleteBookmark(id: String)
}

fun BookmarkEntity.toBookmark() = Bookmark(
    id = id,
    bookmarkUrl = url,
    createdAt = createdAt,
    lastReadAt = lastReadAt,
    isArchived = isReviewed,
    notes = notes,
    mainImage = mainImage,
    title = title,
    description = description,
    siteName = siteName,
    favIcon = favIcon,
    tagIds = tagsIds
)

class BookmarkDataSourceImpl constructor(
    private val database: BrainmarkDB,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookmarkDataSource {

    private val queries = database.brainmarkDBQueries

    override suspend fun getReadingRecommendations(): List<Bookmark> {
        return withContext(dispatcher) {
            queries.getReadingRecommendation().asBookmarks()
        }
    }

    override suspend fun getRevisionRecommendations(): List<Bookmark> {
        return withContext(dispatcher) {
            queries.getReadingRecommendation().asBookmarks()
        }
    }

    override suspend fun getBookmarksByLastReadPaging(limit: Long, offset: Long): List<Bookmark> {
        return withContext(dispatcher) {
            queries.getBookmarksByLastReadPaging(limit, offset).asBookmarks()
        }
    }

    override suspend fun getAllBookmarksPaging(limit: Long, offset: Long): List<Bookmark> {
        return queries.getAllBookmarksPaging(limit, offset).asBookmarks()
    }

    override suspend fun getBookmarksByTagPaging(
        tagId: String,
        limit: Long,
        offset: Long
    ): List<Bookmark> {
        return queries.getBookmarksByTagPaging(tagId, limit, offset).asBookmarks()
    }

    override suspend fun searchAllBookmarksPaging(
        keyword: String,
        limit: Long,
        offset: Long
    ): List<Bookmark> {
        return queries
            .searchAllBookmarkPaging(keyword, limit, offset)
            .executeAsList()
            .map { it.toBookmark() }
    }

    override suspend fun searchBookmarksByTagPaging(
        keyword: String,
        tagId: String,
        limit: Long,
        offset: Long
    ): List<Bookmark> {
        return queries.searchBookmarkByTagPaging(tagId, keyword, limit, offset)
            .executeAsList()
            .map { it.toBookmark() }
    }

    override suspend fun getBookmarkOrDefault(id: String, default: () -> Bookmark): Bookmark {
        return withContext(dispatcher) {
            queries
                .getBookmarksById(id)
                .executeAsOneOrNull()
                ?.toBookmark() ?: default.invoke()
        }
    }

    override suspend fun upsertBookmark(bookmark: Bookmark): String = withContext(dispatcher) {
        queries.upsertBookmark(
            id = bookmark.id,
            url = bookmark.bookmarkUrl,
            createdAt = bookmark.createdAt,
            lastReadAt = bookmark.lastReadAt,
            isReviewed = bookmark.isArchived,
            notes = bookmark.notes,
            mainImage = bookmark.mainImage,
            title = bookmark.title,
            description = bookmark.description,
            siteName = bookmark.siteName,
            favIcon = bookmark.favIcon,
            tagsIds = bookmark.tagIds
        )
        return@withContext bookmark.id
    }

    override suspend fun deleteBookmark(id: String) = withContext(dispatcher) {
        queries.deleteBookmark(id)
    }

    override suspend fun getBookmarkByUrlOrDefault(url: String, default: () -> Bookmark): Bookmark {
        return queries.getBookmarksByUrl(url)
            .executeAsOneOrNull()
            ?.toBookmark() ?: default.invoke()
    }

    private fun Query<BookmarkEntity>.asBookmarks(): List<Bookmark> {
        return executeAsList().map {
            it.toBookmark()
        }
    }

    private fun SearchAllBookmarkPaging.toBookmark() = Bookmark(
        id = id,
        bookmarkUrl = url,
        createdAt = createdAt,
        lastReadAt = lastReadAt,
        isArchived = isReviewed,
        notes = notes,
        mainImage = mainImage,
        title = title,
        description = description,
        siteName = siteName,
        favIcon = favIcon,
        tagIds = tagsIds
    )

    private fun SearchBookmarkByTagPaging.toBookmark() = Bookmark(
        id = id,
        bookmarkUrl = url,
        createdAt = createdAt,
        lastReadAt = lastReadAt,
        isArchived = isReviewed,
        notes = notes,
        mainImage = mainImage,
        title = title,
        description = description,
        siteName = siteName,
        favIcon = favIcon,
        tagIds = tagsIds
    )


}
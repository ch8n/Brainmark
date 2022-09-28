package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ch8n.common.BookmarkEntity
import dev.ch8n.common.SearchAllBookmarkPaging
import dev.ch8n.common.SearchBookmarkByTagPaging
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface BookmarkDataSource {
    fun getAllBookmarks(): Flow<List<Bookmark>>
    fun getReadingRecommendations(): List<Bookmark>
    suspend fun getBookmarkByUrl(url: String): Bookmark?
    suspend fun getBookmarkById(id: String): Bookmark?
    suspend fun deleteBookmark(id: String)
    suspend fun upsertBookmark(bookmark: Bookmark): String
    suspend fun getBookmarksByLastReadPaging(limit: Long, offset: Long): List<Bookmark>
    suspend fun getRevisionBookmarks(): List<Bookmark>
    suspend fun allBookmarksPaging(limit: Long, offset: Long): List<Bookmark>
    suspend fun bookmarksByTagPaging(tagId: String, limit: Long, offset: Long): List<Bookmark>
    suspend fun untaggedBookmarksPaging(limit: Long, offset: Long): List<Bookmark>
    suspend fun searchAllBookmarksPaging(keyword: String, limit: Long, offset: Long): List<Bookmark>
    suspend fun searchBookmarksByTagPaging(keyword: String, tagId: String, limit: Long, offset: Long): List<Bookmark>
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

    override suspend fun getBookmarkByUrl(url: String): Bookmark? {
        return queries.getBookmarksByUrl(url).executeAsOneOrNull()?.toBookmark()
    }

    override fun getReadingRecommendations(): List<Bookmark> {
        return queries.getRandomBookmarks().executeAsList().map {
            it.toBookmark()
        }
    }

    override fun getAllBookmarks(): Flow<List<Bookmark>> = queries.getBookmarks()
        .asFlow()
        .distinctUntilChanged()
        .mapToList(dispatcher)
        .map { entities ->
            entities.map { it.toBookmark() }
        }

    override suspend fun allBookmarksPaging(limit: Long, offset: Long): List<Bookmark> {
        return queries.getBookmarksPaging(limit, offset).executeAsList().map { it.toBookmark() }
    }

    override suspend fun untaggedBookmarksPaging(limit: Long, offset: Long): List<Bookmark> {
        return queries.getUntaggedBookmarks(limit, offset).executeAsList().map { it.toBookmark() }
    }

    override suspend fun bookmarksByTagPaging(tagId: String, limit: Long, offset: Long): List<Bookmark> {
        return queries.getBookmarksByTagPaging(tagId, limit, offset).executeAsList().map { it.toBookmark() }
    }

    override suspend fun searchAllBookmarksPaging(keyword: String, limit: Long, offset: Long): List<Bookmark> {
        return queries
            .searchAllBookmarkPaging(keyword, limit, offset)
            .executeAsList()
            .map { it.toBookmark() }
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

    override suspend fun getBookmarkById(id: String): Bookmark? = withContext(dispatcher) {
        queries.getBookmarksById(id).executeAsOneOrNull()?.toBookmark()
    }

    override suspend fun getBookmarksByLastReadPaging(limit: Long, offset: Long): List<Bookmark> =
        withContext(dispatcher) {
            queries.getBookmarksByLastReadPaging(limit, offset).executeAsList().map {
                it.toBookmark()
            }
        }

    override suspend fun getRevisionBookmarks(): List<Bookmark> {
        return withContext(dispatcher) {
            queries.getRevisionBookmarks().executeAsList().map {
                it.toBookmark()
            }
        }
    }

    override suspend fun deleteBookmark(id: String) = withContext(dispatcher) {
        queries.deleteBookmark(id)
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
}
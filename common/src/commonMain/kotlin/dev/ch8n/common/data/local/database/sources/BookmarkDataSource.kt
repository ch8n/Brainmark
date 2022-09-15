package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ch8n.common.BookmarkEntity
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface BookmarkDataSource {
    fun getAllBookmarks(): Flow<List<Bookmark>>
    suspend fun getBookmarkByUrl(url: String): Bookmark?
    suspend fun getBookmarkById(id: String): Bookmark?
    suspend fun getBookmarksByIds(ids: List<String>): List<Bookmark>
    suspend fun deleteBookmark(id: String)
    suspend fun createBookmark(bookmark: Bookmark): String
    suspend fun updateBookmark(bookmark: Bookmark): String
    suspend fun getBookmarksPaging(limit: Long, offset: Long): List<Bookmark>
}

fun BookmarkEntity.toBookmark() = Bookmark(
    id = id,
    bookmarkUrl = url,
    createdAt = createdAt,
    isArchived = isReviewed,
    notes = notes,
    mainImage = mainImage,
    title = title,
    description = description,
    siteName = siteName,
    favIcon = favIcon,
    tagIds = tagsIds,
    flashCardIds = flashCardsIds
)

class BookmarkDataSourceImpl constructor(
    private val database: BrainmarkDB,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookmarkDataSource {

    private val queries = database.brainmarkDBQueries

    override suspend fun getBookmarkByUrl(url: String): Bookmark? {
        return queries.getBookmarksByUrl(url).executeAsOneOrNull()?.toBookmark()
    }

    override fun getAllBookmarks(): Flow<List<Bookmark>> = queries.getBookmarks()
        .asFlow()
        .distinctUntilChanged()
        .mapToList(dispatcher)
        .map { entities ->
            entities.map { it.toBookmark() }
        }

    override suspend fun getBookmarksPaging(limit: Long, offset: Long): List<Bookmark> {
        return queries.getBookmarksPaging(limit, offset).executeAsList().map { it.toBookmark() }
    }

    override suspend fun getBookmarkById(id: String): Bookmark? = withContext(dispatcher) {
        queries.getBookmarksById(id).executeAsOneOrNull()?.toBookmark()
    }


    override suspend fun getBookmarksByIds(ids: List<String>): List<Bookmark> = withContext(dispatcher) {
        coroutineScope {
            ids.map { id -> async { getBookmarkById(id) } }
                .awaitAll()
                .filterNotNull()
        }
    }

    override suspend fun deleteBookmark(id: String) = withContext(dispatcher) {
        queries.deleteBookmark(id)
    }

    override suspend fun createBookmark(bookmark: Bookmark): String = withContext(dispatcher) {
        queries.upsertBookmark(
            id = bookmark.id,
            url = bookmark.bookmarkUrl,
            createdAt = bookmark.createdAt,
            isReviewed = bookmark.isArchived,
            notes = bookmark.notes,
            mainImage = bookmark.mainImage,
            title = bookmark.title,
            description = bookmark.description,
            siteName = bookmark.siteName,
            favIcon = bookmark.favIcon,
            tagsIds = bookmark.tagIds,
            flashCardsIds = bookmark.flashCardIds
        )
        return@withContext bookmark.id
    }

    override suspend fun updateBookmark(bookmark: Bookmark): String = createBookmark(bookmark)
}
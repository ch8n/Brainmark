package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ch8n.common.BookmarkEntity
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

interface BookmarkDataSource {
    fun getAllBookmarks(): Flow<List<Bookmark>>
    suspend fun getBookmarkById(id: String): Bookmark?
    suspend fun getBookmarksByIds(id: List<String>): List<Bookmark>
    suspend fun deleteBookmark(ids: String)
    suspend fun createBookmark(bookmark: Bookmark): String
    suspend fun updateBookmark(bookmark: Bookmark): String
}

fun BookmarkEntity.toBookmark() = Bookmark(
    id = id,
    url = url,
    tagsIds = tags,
    createdAt = createdAt,
    remindAt = remindAt,
    isReviewed = isReviewed,
    notes = notes
)

class BookmarkDataSourceImpl constructor(
    private val database: BrainmarkDB,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookmarkDataSource {

    private val queries = database.brainmarkDBQueries

    override fun getAllBookmarks(): Flow<List<Bookmark>> = queries.getBookmarks()
        .asFlow()
        .distinctUntilChanged()
        .mapToList(dispatcher)
        .map { entities -> entities.map { it.toBookmark() } }

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
            url = bookmark.url,
            createdAt = bookmark.createdAt,
            remindAt = bookmark.remindAt,
            notes = bookmark.notes,
            tags = bookmark.tagsIds,
            isReviewed = bookmark.isReviewed
        )
        return@withContext bookmark.id
    }

    override suspend fun updateBookmark(bookmark: Bookmark): String = createBookmark(bookmark)
}
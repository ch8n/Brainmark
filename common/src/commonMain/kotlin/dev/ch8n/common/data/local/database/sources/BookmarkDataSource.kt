package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface BookmarkDataSource {
    fun getAllBookmarks(): Flow<List<Bookmark>>
    suspend fun getBookmarkById(id: String): Bookmark?
    suspend fun getBookmarksByIds(ids: List<String>): List<Bookmark>
    suspend fun deleteBookmark(id: String)
    suspend fun createBookmark(bookmark: Bookmark): String
    suspend fun updateBookmark(bookmark: Bookmark): String
}

//fun BookmarkEntity.toBookmark() = Bookmark(
//    id = id,
//    url = url,
//    createdAt = createdAt,
//    remindAt = remindAt,
//    isArchived = isReviewed,
//    notes = notes,
//    // TODO fix
//    meta = Meta.default,
//    primaryTagId = "",
//    secondaryTagIds = tags
//)

class BookmarkDataSourceImpl constructor(
    private val database: BrainmarkDB,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookmarkDataSource {

    private val queries = database.brainmarkDBQueries

    override fun getAllBookmarks(): Flow<List<Bookmark>> = queries.getBookmarks()
        .asFlow()
        .distinctUntilChanged()
        .mapToList(dispatcher)
        .map { entities ->
            entities.map {
                //TODO fix it.toBookmark()
                Bookmark.SAMPLE
            }
        }

    override suspend fun getBookmarkById(id: String): Bookmark? = withContext(dispatcher) {
        //queries.getBookmarksById(id).executeAsOneOrNull()?.toBookmark()
        //TODO fix
        null
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
        // TODO add more fields
        queries.upsertBookmark(
            id = bookmark.id,
            url = bookmark.bookmarkUrl,
            createdAt = bookmark.createdAt,
            isReviewed = false,
            notes = "",
            tags = bookmark.tagIds
        )
        return@withContext bookmark.id
    }

    override suspend fun updateBookmark(bookmark: Bookmark): String = createBookmark(bookmark)
}
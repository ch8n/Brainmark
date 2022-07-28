package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ch8n.common.TagEntity
import dev.ch8n.common.data.model.Tags
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface TagsDataSource {
    fun getAllTags(): Flow<List<Tags>>
    suspend fun getTagById(id: String): Tags?
    suspend fun deleteTag(id: String)
    suspend fun createTag(tag: Tags): String
    suspend fun updateTag(tag: Tags): String
    suspend fun getTagsByIds(ids: List<String>): List<Tags>
}

fun TagEntity.toTags() = Tags(
    id = id,
    name = name,
    color = color
)

class TagsDataSourceImpl constructor(
    private val database: BrainmarkDB,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TagsDataSource {

    private val queries = database.brainmarkDBQueries

    override fun getAllTags(): Flow<List<Tags>> = queries
        .getAllTags()
        .asFlow()
        .distinctUntilChanged()
        .mapToList(dispatcher)
        .map { entities -> entities.map { it.toTags() } }

    override suspend fun getTagById(id: String): Tags? = withContext(dispatcher) {
        queries.getTagById(id).executeAsOneOrNull()?.toTags()
    }

    override suspend fun deleteTag(id: String): Unit = withContext(dispatcher) {
        queries.deleteTag(id)
    }

    override suspend fun createTag(tag: Tags): String = withContext(dispatcher) {
        queries.upsertTag(tag.id, tag.name, tag.color)
        return@withContext tag.id
    }

    override suspend fun updateTag(tag: Tags): String = createTag(tag)

    override suspend fun getTagsByIds(ids: List<String>): List<Tags> = withContext(dispatcher) {
        coroutineScope {
            ids
                .map { id -> async { getTagById(id) } }
                .awaitAll()
                .filterNotNull()
        }
    }
}
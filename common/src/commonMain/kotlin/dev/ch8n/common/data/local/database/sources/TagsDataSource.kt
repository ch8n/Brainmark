package dev.ch8n.common.data.local.database.sources

import dev.ch8n.common.TagEntity
import dev.ch8n.common.data.model.Tags
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

interface TagsDataSource {
    suspend fun getAllTags(limit: Long, offset: Long): List<Tags>
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

    override suspend fun getAllTags(limit: Long, offset: Long): List<Tags> = queries
        .getAllTags(limit, offset)
        .executeAsList()
        .map { it.toTags() }

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
            ids.map { id -> async { getTagById(id) } }
                .awaitAll()
                .filterNotNull()
        }
    }
}
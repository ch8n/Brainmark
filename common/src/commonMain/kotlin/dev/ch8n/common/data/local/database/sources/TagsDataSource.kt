package dev.ch8n.common.data.local.database.sources

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ch8n.common.TagEntity
import dev.ch8n.common.data.model.Tags
import dev.ch8n.sqlDB.BrainmarkDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface TagsDataSource {
    fun getAllTags(): Flow<List<Tags>>
    suspend fun getTagById(id: String): Tags?
    suspend fun deleteTag(id: String)
    suspend fun createTag(tag: Tags)
    suspend fun updateTag(tag: Tags)
}

fun TagEntity.toTags() = Tags(
    id = id,
    name = name
)

class TagsDataSourceImpl constructor(
    private val database: BrainmarkDB,
    private val dispatcher: CoroutineDispatcher
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

    override suspend fun createTag(tag: Tags): Unit = withContext(dispatcher) {
        queries.upsertTag(tag.id, tag.name)
    }

    override suspend fun updateTag(tag: Tags) = createTag(tag)
}
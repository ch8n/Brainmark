package dev.ch8n.common.domain.usecases

import dev.ch8n.common.data.local.database.sources.TagsDataSource
import dev.ch8n.common.data.model.Tags
import kotlinx.coroutines.flow.flow

class TagUseCases(
    val getAllTags: GetAllTagsUseCase,
    val getTagByName: GetTagByNameUseCase,
    val getTagById: GetTagByIdUseCase,
    val getTagsByIds: GetTagsByIdsUseCase,
    val upsertTag: UpsertTagUseCase,
    val deleteTag: DeleteTagUseCase
)

class GetTagsByIdsUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(tagIds: List<String>) = flow {
        val tags = tagsDataSource.getTagsByIds(tagIds)
        emit(tags)
    }
}

class GetTagByNameUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(name: String) = flow {
        val tags = tagsDataSource.getTagByName(name) ?: Tags.Empty
        emit(tags)
    }
}

class GetAllTagsUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(limit: Long, offset: Long) = flow {
        val tags = tagsDataSource.getAllTags(limit, offset)
        emit(tags)
    }
}

class GetTagByIdUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(id: String) = flow {
        val tag = tagsDataSource.getTagById(id = id) ?: Tags.Empty
        emit(tag)
    }
}

class UpsertTagUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(tag: Tags) = flow {
        val newId = tagsDataSource.upsertTag(tag = tag)
        emit(newId)
    }
}

class DeleteTagUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(id: String) = flow {
        tagsDataSource.deleteTag(id)
        emit(Unit)
    }
}
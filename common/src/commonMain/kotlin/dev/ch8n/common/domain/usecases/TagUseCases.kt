package dev.ch8n.common.domain.usecases

import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.local.database.sources.TagsDataSource
import dev.ch8n.common.data.model.Tags
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TagUseCases(
    val getAllTagsUseCase: GetAllTagsUseCase,
    val getTagByIdUseCase: GetTagByIdUseCase,
    val getTagsByIdsUseCase: GetTagsByIdsUseCase,
    val createTagUseCase: CreateTagUseCase,
    val updateTagUseCase: UpdateTagUseCase,
    val deleteTagUseCase: DeleteTagUseCase
)

class GetTagsByIdsUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(tagIds: List<String>) = flow {
        val tags = tagsDataSource.getTagsByIds(tagIds)
        emit(tags)
    }
}

class GetAllTagsUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke() = tagsDataSource.getAllTags()
}

class GetTagByIdUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(id: String) = flow {
        val tag = tagsDataSource.getTagById(id = id) ?: error("Tag not found")
        emit(tag)
    }
}

class UpdateTagUseCase(
    private val tagsDataSource: TagsDataSource,
    private val getTagByIdUseCase: GetTagByIdUseCase,
) {
    operator fun invoke(id: String, name: String) = getTagByIdUseCase(id)
        .map { tag ->
            val updated = tag.copy(name = name)
            tagsDataSource.updateTag(updated)
        }
}

class CreateTagUseCase(
    private val tagsDataSource: TagsDataSource
) {
    operator fun invoke(name: String) = flow {
        val id = tagsDataSource.createTag(
            tag = Tags(
                id = uuid4().toString(),
                name = name,
                //TODO fix
                color = ""
            )
        )
        emit(id)
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
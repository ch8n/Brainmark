package dev.ch8n.common.ui.screens

import dev.ch8n.common.data.model.Tags
import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.domain.usecases.TagUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TagScreenController(
    private val componentContext: ComponentContext,
    private val tagUseCases: TagUseCases,
) : ComponentContext by componentContext {

    private val _tag = MutableStateFlow(Tags.EMPTY)
    val tag: StateFlow<Tags> = _tag.asStateFlow()

    fun onTagTextChange(tag: String) {
        changeState {
            copy(name = tag)
        }
    }

    fun createNewTag(tag: Tags) = with(tag) {
        tagUseCases.createTagUseCase(id, name)
    }

    fun updateTag(tag: Tags) = with(tag) {
        tagUseCases.updateTagUseCase(id, name)
    }

    private inline fun changeState(reducer: Tags.() -> Tags): Tags {
        val updatedTag = tag.value.reducer()
        _tag.value = updatedTag
        return updatedTag
    }
}
package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Tags
import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.domain.usecases.TagUseCases
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TagScreenController(
    componentContext: ComponentContext,
    private val tagUseCases: TagUseCases,
) : DecomposeController(componentContext) {

    private val _tag = MutableStateFlow(Tags.EMPTY)
    val tag: StateFlow<Tags> = _tag.asStateFlow()

    fun onTagTextChange(tag: String) {
        changeState {
            copy(name = tag)
        }
    }

    fun createNewTag(tag: Tags) = with(tag) {
        tagUseCases.createTagUseCase(name)
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
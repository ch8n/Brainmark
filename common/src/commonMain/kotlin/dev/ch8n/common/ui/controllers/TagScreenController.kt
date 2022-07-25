package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Tags
import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TagScreenController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    private val updateTagUseCase = DomainInjector.tagUseCase.updateTagUseCase
    private val createTagUseCase = DomainInjector.tagUseCase.createTagUseCase

    private val _tag = MutableStateFlow(Tags.TAG_KOTLIN)
    val tag: StateFlow<Tags> = _tag.asStateFlow()

    fun onTagTextChange(tag: String) {
        changeState {
            copy(name = tag)
        }
    }
    fun createNewTag(tag: Tags) = with(tag) {
        createTagUseCase(name)
    }

    fun updateTag(tag: Tags) = with(tag) {
        updateTagUseCase(id, name)
    }

    private inline fun changeState(reducer: Tags.() -> Tags): Tags {
        val updatedTag = tag.value.reducer()
        _tag.value = updatedTag
        return updatedTag
    }
}
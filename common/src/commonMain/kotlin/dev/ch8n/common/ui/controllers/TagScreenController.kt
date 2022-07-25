package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TagScreenController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    private val updateTagUseCase = DomainInjector.tagUseCase.updateTagUseCase
    private val createTagUseCase = DomainInjector.tagUseCase.createTagUseCase

    private val sampleTags = listOf(
        Tags.TAG_KOTLIN,
        Tags.TAG_JAVA,
        Tags.TAG_KMM,
        Tags.TAG_WEB_DEV,
        Tags.TAG_KMP
    )

    private val _tags = MutableStateFlow(sampleTags)
    val tags = _tags.asStateFlow()

    fun saveTag(tag: Tags) {
        val currentList = _tags.value
        val isContained = currentList.any { it.id == tag.id }
        val updatedList = if (isContained) {
            currentList.map {
                if (it.id == tag.id) tag else it
            }
        } else {
            currentList.toMutableList().apply {
                add(tag)
            }
        }
        _tags.tryEmit(updatedList)
    }

    fun deleteTag(tag: Tags) {
        val currentList = _tags.value
        val updated = currentList.filter { it.id != tag.id }
        _tags.tryEmit(updated)
    }


//    private inline fun changeState(reducer: Tags.() -> Tags): Tags {
//        val updatedTag = tag.value.reducer()
//        _tag.value = updatedTag
//        return updatedTag
//    }
}
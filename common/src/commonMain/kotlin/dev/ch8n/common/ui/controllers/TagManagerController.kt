package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import dev.ch8n.common.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class TagManagerController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    private val createTag = DomainInjector
        .tagUseCase
        .createTagUseCase

    private val deleteTag = DomainInjector
        .tagUseCase
        .deleteTagUseCase

    val getAllTags = DomainInjector
        .tagUseCase
        .getAllTagsUseCase()

    val tagName = MutableStateFlow("")
    val tagColor = MutableStateFlow("")
    val error = MutableStateFlow("")
    val isLoading = MutableStateFlow(false)

    suspend fun saveTag() {
        val name = tagName.value
        val color = tagColor.value
        val result = Result.build { createTag.invoke(name, color).first() }
        if (result is Result.Error) {
            error.value = result.error.message
                ?: "Something went wrong!"
        }
    }

    suspend fun deleteTag(tag: Tags) {
        val id = tag.id
        val result = Result.build { deleteTag.invoke(id).first() }
        if (result is Result.Error) {
            error.value = result.error.message
                ?: "Something went wrong!"
        }
    }
}
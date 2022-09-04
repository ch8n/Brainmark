package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TagManagerController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    @Immutable
    data class ViewState(
        val selectedId: String,
        val tagName: String,
        val tagColor: Color,
        val errorMsg: String,
        val isLoading: Boolean
    ) {
        companion object {
            val Initial = ViewState(
                selectedId = "",
                tagName = "",
                tagColor = ColorsUtils.randomColor,
                errorMsg = "",
                isLoading = false
            )
        }
    }

    val state = MutableStateFlow(ViewState.Initial)

    private val createTag = DomainInjector
        .tagUseCase
        .createTagUseCase

    private val deleteTag = DomainInjector
        .tagUseCase
        .deleteTagUseCase

    val getAllTags = DomainInjector
        .tagUseCase
        .getAllTagsUseCase()

    fun saveTag() {
        state.update { it.copy(isLoading = true, errorMsg = "") }
        launch {
            state.update {
                // check and update id
                var id = it.selectedId
                if (id.isEmpty()) {
                    id = uuid4().toString()
                }
                // check tag name
                val name = it.tagName
                if (name.length < 3) {
                    return@update ViewState.Initial.copy(
                        errorMsg = "Name > 2 characters"
                    )
                }

                // check if exist
                val isAlreadyPresent = getAllTags.first()
                    .stream()
                    .anyMatch { it.name.characterAreSame(name) }

                if (isAlreadyPresent) {
                    return@update ViewState.Initial.copy(
                        errorMsg = "Already Exist!"
                    )
                }

                // check tag color
                val color = it.tagColor

                // save or update tag db
                val result = Result.build {
                    createTag.invoke(id, name, color.toDbString()).first()
                }

                // collect result
                return@update when (result) {
                    is Result.Error -> {
                        it.copy(
                            errorMsg = result.error.message ?: "Something went wrong!",
                            isLoading = false
                        )
                    }

                    is Result.Success -> {
                        ViewState.Initial
                    }
                }
            }
        }
    }

    fun updateTagName(name: String) {
        state.update {
            it.copy(tagName = name)
        }
    }

    fun deleteTag() {
        state.update { it.copy(isLoading = true, errorMsg = "") }
        launch {
            state.update {
                // get id
                val id = it.selectedId
                if (id.isEmpty()) {
                    // a new tag
                    return@update ViewState.Initial
                }

                // delete tag in db
                val result = Result.build { deleteTag.invoke(id).first() }

                // collect result
                when (result) {
                    is Result.Error -> {
                        it.copy(
                            errorMsg = result.error.message ?: "Something went wrong!",
                            isLoading = false
                        )
                    }

                    is Result.Success -> {
                        ViewState.Initial
                    }
                }
            }
        }
    }

    fun selectTag(tag: Tags) {
        state.update {
            it.copy(
                selectedId = tag.id,
                tagName = tag.name,
                tagColor = tag.color.toColor(),
                errorMsg = ""
            )
        }
    }


    fun updateTagColor(color: Color) {
        state.update {
            it.copy(
                tagColor = color
            )
        }
    }

    fun clearSelectedTag() {
        state.update {
            ViewState.Initial
        }
    }
}


package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.benasher44.uuid.uuid4
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

    @Immutable
    data class ViewState(
        val selectedId: String,
        val tagName: String,
        val tagColor: String,
        val errorMsg: String,
        val isLoading: Boolean
    ) {
        companion object {
            val Initial = ViewState(
                selectedId = "",
                tagName = "",
                tagColor = "",
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

    suspend fun saveTag() {
        changeState { copy(isLoading = true, errorMsg = "") }
        changeState {
            // check and update id
            var id = state.value.selectedId
            if (id.isEmpty()) {
                id = uuid4().toString()
            }

            // check tag name
            val name = tagName
            if (name.length < 3) {
                return@changeState copy(
                    errorMsg = "Name should be more than 2 characters"
                )
            }

            // check tag color
            val color = tagColor

            // save or update tag db
            val result = Result.build {
                createTag.invoke(id, name, color).first()
            }

            // collect result
            return@changeState when (result) {
                is Result.Error -> {
                    copy(
                        errorMsg = result.error.message ?: "Something went wrong!",
                        isLoading = false
                    )
                }
                is Result.Success -> {
                    copy(
                        selectedId = "",
                        tagName = "",
                        tagColor = "",
                        isLoading = false,
                        errorMsg = ""
                    )
                }
            }
        }
    }

    fun updateTagName(name: String) {
        changeState {
            copy(
                tagName = name
            )
        }
    }

    suspend fun deleteTag() {
        changeState { copy(isLoading = true, errorMsg = "") }
        changeState {
            // get id
            val id = selectedId
            if (id.isEmpty()) {
                // a new tag
                return@changeState copy(
                    selectedId = "",
                    tagName = "",
                    tagColor = "",
                    errorMsg = "",
                    isLoading = false
                )
            }

            // delete tag in db
            val result = Result.build { deleteTag.invoke(id).first() }

            // collect result
            when (result) {
                is Result.Error -> {
                    copy(
                        errorMsg = result.error.message ?: "Something went wrong!",
                        isLoading = false
                    )
                }
                is Result.Success -> {
                    copy(
                        selectedId = "",
                        tagName = "",
                        tagColor = "",
                        errorMsg = "",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectTag(tag: Tags) {
        changeState {
            copy(
                selectedId = tag.id,
                tagName = tag.name,
                tagColor = tag.color
            )
        }
    }

    private inline fun changeState(reducer: ViewState.() -> ViewState): ViewState {
        val updatedState = state.value.reducer()
        state.value = updatedState
        return updatedState
    }
}


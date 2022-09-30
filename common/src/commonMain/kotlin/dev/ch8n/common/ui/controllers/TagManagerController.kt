package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.controllers.TagManagerController.ScreenState.Companion.reset
import dev.ch8n.common.ui.controllers.TagManagerController.ScreenState.Companion.toTag
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.ColorsUtils
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class TagManagerController(
    navController: NavController
) : UiController(navController) {

    @Stable
    data class ScreenState(
        val selectedId: String,
        val tagName: String,
        val tagColor: Color,
        val errorMsg: String,
        val isLoading: Boolean,
        val tags: List<Tags>
    ) {
        companion object {
            val reset
                get() = ScreenState(
                    selectedId = "",
                    tagName = "",
                    tagColor = ColorsUtils.randomColor,
                    errorMsg = "",
                    isLoading = false,
                    tags = emptyList()
                )

            fun ScreenState.toTag() = Tags(
                id = selectedId,
                name = tagName,
                color = tagColor.toArgb()
            )
        }
    }

    val screenState = MutableStateFlow(reset)

    fun withLoading(action: suspend () -> Unit) {
        screenState.update {
            it.copy(
                isLoading = true,
                errorMsg = ""
            )
        }
        try {
            launch { action.invoke() }
        } finally {
            screenState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    // read
    private val getAllTags = DomainInjector
        .tagUseCase
        .getAllTags

    private val getTagByName = DomainInjector
        .tagUseCase
        .getTagByName

    fun nextTags() {
        val current = screenState.value.tags
        val limit = 5L
        val offset = current.size.toLong()
        getAllTags
            .invoke(limit, offset)
            .onEach { nextTags ->
                val updated = current + nextTags
                screenState.update {
                    it.copy(
                        tags = updated
                    )
                }
            }.onceIn(this)
    }

    // create and update
    private val upsertTag = DomainInjector
        .tagUseCase
        .upsertTag

    fun updateTagName(name: String) {
        screenState.update {
            it.copy(tagName = name)
        }
    }

    fun onTagCreate() {
        withLoading {
            val current = screenState.value

            // validate tag name
            val name = current.tagName
            if (name.length < 3) {
                setError("Name > 2 characters")
                return@withLoading
            }

            var id = current.selectedId
            val isNewTag = id.isEmpty()

            if (isNewTag) {
                // create tag
                // check if exist
                getTagByName.invoke(name)
                    .onEach {
                        if (it == Tags.Empty) {
                            // save to db
                            saveTag(current)
                        } else {
                            // if exist error
                            setError("Already Exist!")
                        }
                    }
                    .onceIn(this)
            } else {
                // update tag
                updateTag(current)
            }
        }
    }

    // delete
    private val deleteTag = DomainInjector
        .tagUseCase
        .deleteTag

    fun onTagDelete() {
        withLoading {
            val current = screenState.value
            val id = current.selectedId
            if (id.isEmpty()) {
                // a new tag
                screenState.update {
                    reset.copy(
                        tags = it.tags
                    )
                }
            } else {
                onTagDelete(id, current)
            }
        }
    }


    private fun setError(msg: String) {
        screenState.update {
            reset.copy(
                errorMsg = msg
            )
        }
    }

    private fun saveTag(current: ScreenState) {
        val newTag = current.toTag().copy(
            id = uuid4().toString()
        )
        upsertTag.invoke(newTag)
            .onEach {
                val updatedTags = mutableListOf<Tags>()
                updatedTags.add(newTag)
                updatedTags.addAll(current.tags)
                screenState.update {
                    reset.copy(
                        tags = updatedTags
                    )
                }
            }
            .catch { error ->
                setError(error.message ?: "Something went wrong!")
            }
            .onceIn(this)
    }

    private fun updateTag(current: ScreenState) {
        val updateTag = current.toTag()
        upsertTag.invoke(updateTag)
            .onEach {
                val updatedTags = current.tags.map { oldTag ->
                    if (oldTag.id == updateTag.id) {
                        updateTag
                    } else {
                        oldTag
                    }
                }
                screenState.update {
                    reset.copy(
                        tags = updatedTags
                    )
                }
            }
            .catch { error ->
                setError(error.message ?: "Something went wrong!")
            }
            .onceIn(this)
    }

    private fun onTagDelete(id: String, current: ScreenState) {
        deleteTag.invoke(id)
            .onEach {
                val updated = current.tags.filter { it.id != id }
                screenState.update {
                    reset.copy(
                        tags = updated
                    )
                }
            }
            .catch { error ->
                setError(error.message ?: "Something went wrong!")
            }
            .onceIn(this)
    }

    fun onTagSelected(tag: Tags) {
        screenState.update {
            it.copy(
                selectedId = tag.id,
                tagName = tag.name,
                tagColor = Color(tag.color),
                errorMsg = "",
            )
        }
    }


    fun updateTagColor(color: Color) {
        screenState.update {
            it.copy(
                tagColor = color
            )
        }
    }

    fun onClearTagName() {
        screenState.update {
            reset.copy(
                tags = it.tags
            )
        }
    }
}


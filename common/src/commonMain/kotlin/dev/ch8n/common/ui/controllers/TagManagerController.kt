package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.ColorsUtils
import dev.ch8n.common.utils.Result
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.characterAreSame
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
        val isLoading: Boolean
    ) {
        companion object {
            val Initial
                get() = ScreenState(
                    selectedId = "",
                    tagName = "",
                    tagColor = ColorsUtils.randomColor,
                    errorMsg = "",
                    isLoading = false
                )
        }
    }

    val state = MutableStateFlow(ScreenState.Initial)

    private val createTag = DomainInjector
        .tagUseCase
        .createTagUseCase

    private val deleteTag = DomainInjector
        .tagUseCase
        .deleteTagUseCase

    private val getAllTags = DomainInjector
        .tagUseCase
        .getAllTagsUseCase

    private val _tags = MutableStateFlow<List<Tags>>(emptyList())
    val tags = _tags.asStateFlow()

    fun nextTags() {
        val current = _tags.value
        val limit = 5L
        val offset = current.size.toLong()
        getAllTags.invoke(limit, offset)
            .onEach { nextTags ->
                val updated = current + nextTags
                _tags.update { updated }
            }.onceIn(this)
    }

    fun saveTag() {
        state.update { it.copy(isLoading = true, errorMsg = "") }
        launch {
            state.update {
                // check and update id
                var id = it.selectedId
                val isNewTag = id.isEmpty()
                if (isNewTag) {
                    id = uuid4().toString()
                }
                // check tag name
                val name = it.tagName
                if (name.length < 3) {
                    return@update ScreenState.Initial.copy(
                        errorMsg = "Name > 2 characters"
                    )
                }

                // check tag color
                val color = it.tagColor.toArgb()

                // check match
                val match: Tags? = tags.first().find {
                    it.name.characterAreSame(name)
                }

                val alreadyExists = isNewTag && match != null
                if (alreadyExists) {
                    return@update ScreenState.Initial.copy(
                        errorMsg = "Already Exist!"
                    )
                }

                if (match != null) {
                    val isColorChanged = match.color != color
                    val isNameChanged = match.name != name
                    if (!isColorChanged && !isNameChanged) {
                        return@update ScreenState.Initial.copy(
                            errorMsg = "Already Exist!"
                        )
                    }
                }

                // save or update tag db
                val result = Result.build {
                    createTag.invoke(id, name, color).first()
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
                        ScreenState.Initial
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
                    return@update ScreenState.Initial
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
                        ScreenState.Initial
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
                tagColor = Color(tag.color),
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
            ScreenState.Initial
        }
    }
}


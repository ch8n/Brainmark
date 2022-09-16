package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.controllers.CreateBookmarkController.ScreenState.Companion.createBookmark
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CreateBookmarkController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    @Immutable
    data class ScreenState(
        val isLoading: Boolean,
        val isError: Boolean,
        val errorMsg: String,
        val title: String,
        val description: String,
        val siteName: String,
        val favIcon: String,
        val mainImage: String,
        val url: String,
        val tagIds: List<String>
    ) {
        companion object {
            fun reset() = ScreenState(
                isLoading = false,
                isError = false,
                errorMsg = "",
                title = "",
                description = "",
                siteName = "",
                favIcon = "",
                mainImage = "",
                url = "",
                tagIds = emptyList()
            )

            fun ScreenState.createBookmark() = Bookmark(
                id = uuid4().toString(),
                tagIds = tagIds,
                createdAt = Clock.System.now().epochSeconds,
                isArchived = false,
                mainImage = mainImage,
                title = title,
                description = description,
                siteName = siteName,
                favIcon = favIcon,
                bookmarkUrl = url,
                flashCardIds = emptyList(),
                notes = ""
            )
        }
    }

    val getAllTags = DomainInjector
        .tagUseCase
        .getAllTagsUseCase()
        .map { it.sortedBy { it.name } }

    private val createBookmarkUseCase = DomainInjector
        .bookmarkUseCase
        .upsertBookmarkUseCase

    private val getBookmarkByUrl = DomainInjector
        .bookmarkUseCase
        .getBookmarkByUrl

    private val htmlService = DomainInjector.htmlParserService

    private val _screenState = MutableStateFlow(ScreenState.reset())
    val screenState = _screenState.asStateFlow()

    val selectedTags = getAllTags.combine(_screenState) { tags, _bookmarkState ->
        return@combine _bookmarkState.tagIds.mapNotNull { id ->
            tags.find { it.id == id }
        }
    }

    private var metaParseJob: Job? = null
    fun onChangeBookmarkUrl(url: String) {
        _screenState.update {
            it.copy(
                isLoading = url.isNotBlank(),
                url = url,
            )
        }
        metaParseJob?.cancel()
        metaParseJob = launch {
            delay(100)
            updateBookmarkMeta(url)
        }
    }

    private suspend fun isAlreadyExistingBookmark(url: String): Boolean {
        val existingBookmark = getBookmarkByUrl.invoke(url).firstOrNull()
        return existingBookmark != null
    }

    private suspend fun updateBookmarkMeta(url: String) {
        val isAlreadyExist = isAlreadyExistingBookmark(url)
        if (isAlreadyExist) {
            _screenState.update {
                it.copy(
                    isLoading = false,
                    errorMsg = "Bookmark already Exist!",
                    isError = true
                )
            }
            return
        }

        val metaOrNull = kotlin.runCatching {
            val html = htmlService.getHtml(url)
            htmlService.parseMeta(url, html)
        }.getOrNull()

        val meta = metaOrNull ?: return
        _screenState.update {
            it.copy(
                isLoading = false,
                isError = false,
                errorMsg = "",
                title = meta.title,
                description = meta.description,
                siteName = meta.authorOrSite,
                favIcon = meta.favIcon,
                mainImage = meta.mainImage,
                url = meta.url
            )
        }
    }

    fun onTitleChanged(title: String) {
        _screenState.update {
            it.copy(title = title)
        }
    }

    fun onDescriptionChanged(description: String) {
        _screenState.update {
            it.copy(description = description)
        }
    }

    fun onAuthorChanged(author: String) {
        _screenState.update {
            it.copy(siteName = author)
        }
    }

    fun onTagAdded(tag: Tags) {
        val current = _screenState.value.tagIds
        val exist = current.any { it == tag.id }
        if (!exist) {
            val updated = current + tag.id
            _screenState.update {
                it.copy(tagIds = updated)
            }
        }
    }

    fun onTagRemoved(tag: Tags) {
        val current = _screenState.value.tagIds
        val updated = current.filter { it != tag.id }
        _screenState.update {
            it.copy(tagIds = updated)
        }
    }

    fun onClickCreateBookmark(
        onSuccess: (value: String) -> Unit,
        onError: (err: String) -> Unit
    ) {
        _screenState.update {
            it.copy(
                isLoading = true,
                errorMsg = "",
                isError = false
            )
        }
        launch {
            val current = _screenState.value
            if (current.url.isEmpty()) {
                _screenState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = "Url isn't added!",
                        isError = true
                    )
                }
                return@launch
            }

            val isAlreadyExist = isAlreadyExistingBookmark(current.url)
            if (isAlreadyExist) {
                _screenState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = "Url isn't added!",
                        isError = true
                    )
                }
                return@launch onError.invoke("bookmark already exist!")
            }

            createBookmarkUseCase
                .invoke(_screenState.value.createBookmark())
                .catch {
                    onError.invoke(it.cause?.message ?: "Something went wrong!")
                }
                .onEach {
                    onSuccess.invoke(it)
                }
                .onCompletion { error ->
                    if (error == null) {
                        _screenState.update { ScreenState.reset() }
                    }
                }
                .onceIn(this)
        }
    }

    fun clearBookmarkUrl() {
        _screenState.update { ScreenState.reset() }
    }
}
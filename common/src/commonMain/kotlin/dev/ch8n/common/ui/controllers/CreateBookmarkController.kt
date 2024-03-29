package dev.ch8n.common.ui.controllers

import androidx.compose.runtime.Stable
import com.benasher44.uuid.uuid4
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.controllers.CreateBookmarkController.ScreenState.Companion.createBookmark
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import dev.ch8n.common.utils.onceIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

abstract class CreateBookmarkController(
    navController: NavController,
    private val url: String?
) : UiController(navController) {

    @Stable
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
                lastReadAt = 0L,
                isArchived = false,
                mainImage = mainImage,
                title = title,
                description = description,
                siteName = siteName,
                favIcon = favIcon,
                bookmarkUrl = url,
                notes = ""
            )
        }
    }

    private val getAllTags = DomainInjector
        .tagUseCase
        .getAllTags

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

    private val getTagByIds = DomainInjector
        .tagUseCase
        .getTagsByIds

    private val createBookmarkUseCase = DomainInjector
        .bookmarkUseCase
        .upsertBookmark

    private val getBookmarkByUrl = DomainInjector
        .bookmarkUseCase
        .getBookmarkByUrl

    private val htmlService = DomainInjector.htmlParserService

    private val _screenState = MutableStateFlow(ScreenState.reset())
    val screenState = _screenState.asStateFlow()

    init {
        _screenState
            .flatMapConcat { screen -> getTagByIds.invoke(screen.tagIds) }
            .onEach { tags -> _selectedTags.update { tags } }
            .launchIn(this)
    }

    private val _selectedTags = MutableStateFlow<List<Tags>>(emptyList())
    val selectedTags = _selectedTags.asStateFlow()

    fun autofillDeeplink() {
        val url = url ?: return
        onChangeBookmarkUrl(url)
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
        return existingBookmark != Bookmark.Empty
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
                        _screenState.update {
                            ScreenState.reset()
                        }
                    }
                }
                .onceIn(this)
        }
    }

    fun clearBookmarkUrl() {
        _screenState.update { ScreenState.reset() }
    }
}
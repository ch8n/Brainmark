package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateBookmarkController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    val getAllTags = DomainInjector
        .tagUseCase
        .getAllTagsUseCase()
        .map { it.sortedBy { it.name } }

    private val createBookmarkUseCase = DomainInjector
        .bookmarkUseCase
        .createBookmarkUseCase

    private val getBookmarkByUrl = DomainInjector
        .bookmarkUseCase
        .getBookmarkByUrl

    private val htmlService = DomainInjector.htmlParserService

    private val _bookmark = MutableStateFlow(Bookmark.new)
    val bookmark: StateFlow<Bookmark> = _bookmark.asStateFlow()

    private val _url = MutableStateFlow(bookmark.value.bookmarkUrl)
    val url: StateFlow<String> = _url.asStateFlow()

    private val _isParsingHtml = MutableStateFlow(false)
    val isParsingHtml: StateFlow<Boolean> = _isParsingHtml.asStateFlow()

    private val _isDuplicateError = MutableStateFlow(false)
    val isDuplicateError = _isDuplicateError.asStateFlow()

    private val _isSavingBookmark = MutableStateFlow(false)
    val isSavingBookmark = _isSavingBookmark.asStateFlow()

    val selectedTags = getAllTags.combine(_bookmark) { tags, bookmark ->
        return@combine bookmark.tagIds.mapNotNull { id -> tags.find { it.id == id } }
    }

    private var metaParseJob: Job? = null
    fun onChangeBookmarkUrl(url: String) {
        _isParsingHtml.update { true }
        _url.update { url }
        metaParseJob?.cancel()
        metaParseJob = launch {
            delay(100)
            updateBookmarkMeta(url)
        }
    }

    private suspend fun isAlreadyExistingBookmark(url: String): Boolean {
        val existingBookmark = getBookmarkByUrl.invoke(url).firstOrNull()
        val isAlreadyExist = existingBookmark != null
        return isAlreadyExist
    }

    private suspend fun updateBookmarkMeta(url: String) {
        try {
            val isAlreadyExist = isAlreadyExistingBookmark(url)
            _isDuplicateError.update { isAlreadyExist }
            if (isAlreadyExist) {
                _isParsingHtml.update { false }
                return
            }
            val html = htmlService.getHtml(url)
            val meta = htmlService.parseMeta(url, html)
            _bookmark.update {
                it.copy(
                    title = meta.title,
                    description = meta.description,
                    siteName = meta.authorOrSite,
                    favIcon = meta.favIcon,
                    mainImage = meta.mainImage,
                    bookmarkUrl = meta.url
                )
            }
        } catch (e: Exception) {
        }
        _isParsingHtml.update { false }
    }

    fun onTitleChanged(title: String) {
        _bookmark.update {
            it.copy(title = title)
        }
    }

    fun onDescriptionChanged(description: String) {
        _bookmark.update { it.copy(description = description) }
    }

    fun onAuthorChanged(author: String) {
        _bookmark.update { it.copy(siteName = author) }
    }

    fun onTagAdded(tag: Tags) {
        val current = _bookmark.value
        val updatedBookmarks = (current.tagIds + tag.id).toSet().toList()
        _bookmark.update {
            it.copy(tagIds = updatedBookmarks)
        }
    }

    fun onTagRemoved(tag: Tags) {
        val current = _bookmark.value
        val updatedBookmarks = (current.tagIds + tag.id).toSet().toList()
        _bookmark.update {
            it.copy(tagIds = updatedBookmarks)
        }
    }

    fun onClickCreateBookmark(
        onSuccess: (value: String) -> Unit,
        onError: (err: String) -> Unit
    ) {
        _isSavingBookmark.update { true }
        launch {
            val current = _bookmark.value
            if (current.bookmarkUrl.isEmpty()) {
                _isSavingBookmark.update { false }
                return@launch onError.invoke("Url isn't added!")
            }

            val isAlreadyExist = isAlreadyExistingBookmark(current.bookmarkUrl)
            if (isAlreadyExist) {
                _isDuplicateError.update { true }
                _isSavingBookmark.update { false }
                return@launch onError.invoke("bookmark already exist!")
            }

            createBookmarkUseCase.invoke(current)
                .catch { onError.invoke(it.cause?.message ?: "Something went wrong!") }
                .onEach { onSuccess.invoke(it) }
                .onCompletion {
                    _isSavingBookmark.update { false }
                }
                .launchIn(this)
        }
    }

    fun clearBookmarkUrl() {
        _url.update { "" }
        _isDuplicateError.update { false }
    }
}
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

    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url.asStateFlow()

    private val _isParsingHtml = MutableStateFlow(false)
    val isParsingHtml: StateFlow<Boolean> = _isParsingHtml.asStateFlow()

    private val _isDuplicateError = MutableStateFlow(false)
    val isDuplicateError = _isDuplicateError.asStateFlow()

    private val _bookmark = MutableStateFlow(Bookmark.new)
    val bookmark: StateFlow<Bookmark> = _bookmark.asStateFlow()

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

    private suspend fun updateBookmarkMeta(url: String) {
        try {
            val existingBookmark = getBookmarkByUrl.invoke(url).firstOrNull()
            val isAlreadyExist = existingBookmark != null
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

    fun getTag(tagId: String) {
        val current = _bookmark.value
        val updatedTagIds = current.tagIds.filter { it != tagId }
        _bookmark.update {
            it.copy(tagIds = updatedTagIds)
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
        launch {
            val current = _bookmark.value
            createBookmarkUseCase.invoke(current)
                .catch { onError.invoke(it.cause?.message ?: "Something went wrong!") }
                .onEach { onSuccess.invoke(it) }
                .launchIn(this)
        }
    }

    fun clearBookmarkUrl() {
        _url.update { "" }
        _isDuplicateError.update { false }
    }
}
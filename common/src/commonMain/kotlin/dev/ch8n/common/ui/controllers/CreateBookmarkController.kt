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

    data class BookmarkState(
        val bookmark: Bookmark,
        val url: String = bookmark.bookmarkUrl,
        val isError: Boolean,
        val errorMsg: String,
        val isLoading: Boolean,
    )

    private val _bookmarkState = MutableStateFlow<BookmarkState>(
        BookmarkState(
            bookmark = Bookmark.new,
            isError = false,
            errorMsg = "",
            isLoading = false
        )
    )

    val bookmarkState = _bookmarkState.asStateFlow()

    val selectedTags = getAllTags.combine(_bookmarkState) { tags, _bookmarkState ->
        return@combine bookmarkState.value.bookmark.tagIds.mapNotNull { id -> tags.find { it.id == id } }
    }

    private var metaParseJob: Job? = null
    fun onChangeBookmarkUrl(url: String) {
        _bookmarkState.update {
            it.copy(
                isLoading = true,
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
        try {
            val isAlreadyExist = isAlreadyExistingBookmark(url)
            if (isAlreadyExist) {
                _bookmarkState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = "Bookmark already Exist!",
                        isError = true
                    )
                }
                return
            }
            val html = htmlService.getHtml(url)
            val meta = htmlService.parseMeta(url, html)
            _bookmarkState.update {
                it.copy(
                    isLoading = false,
                    isError = false,
                    errorMsg = "",
                    bookmark = it.bookmark.copy(
                        title = meta.title,
                        description = meta.description,
                        siteName = meta.authorOrSite,
                        favIcon = meta.favIcon,
                        mainImage = meta.mainImage,
                        bookmarkUrl = meta.url
                    )
                )
            }
        } catch (e: Exception) {
        }
    }

    fun onTitleChanged(title: String) {
        _bookmarkState.update {
            it.copy(
                bookmark = it.bookmark.copy(
                    title = title
                )
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _bookmarkState.update {
            it.copy(
                bookmark = it.bookmark.copy(
                    description = description
                )
            )
        }
    }

    fun onAuthorChanged(author: String) {
        _bookmarkState.update {
            it.copy(
                bookmark = it.bookmark.copy(
                    siteName = author
                )
            )
        }
    }

    fun onTagAdded(tag: Tags) {
        val current = _bookmarkState.value.bookmark
        val updatedBookmarks = (current.tagIds + tag.id).toSet().toList()
        _bookmarkState.update {
            it.copy(
                bookmark = it.bookmark.copy(
                    tagIds = updatedBookmarks
                ),
            )
        }
    }

    fun onTagRemoved(tag: Tags) {
        val current = _bookmarkState.value.bookmark
        val updatedBookmarks = (current.tagIds + tag.id).toSet().toList()
        _bookmarkState.update {
            it.copy(
                bookmark = it.bookmark.copy(
                    tagIds = updatedBookmarks
                ),
            )
        }
    }

    fun onClickCreateBookmark(
        onSuccess: (value: String) -> Unit,
        onError: (err: String) -> Unit
    ) {
        _bookmarkState.update {
            it.copy(
                isLoading = true,
                errorMsg = "",
                isError = false
            )
        }
        launch {
            val current = _bookmarkState.value.bookmark
            if (current.bookmarkUrl.isEmpty()) {
                _bookmarkState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = "Url isn't added!",
                        isError = true
                    )
                }
                return@launch
            }

            val isAlreadyExist = isAlreadyExistingBookmark(current.bookmarkUrl)
            if (isAlreadyExist) {
                _bookmarkState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = "Url isn't added!",
                        isError = true
                    )
                }
                return@launch onError.invoke("bookmark already exist!")
            }

            createBookmarkUseCase.invoke(current)
                .catch { onError.invoke(it.cause?.message ?: "Something went wrong!") }
                .onEach { onSuccess.invoke(it) }
                .onCompletion {
                    _bookmarkState.update {
                        it.copy(
                            isLoading = false,
                            errorMsg = "",
                            isError = false
                        )
                    }
                }
                .launchIn(this)
        }
    }

    fun clearBookmarkUrl() {
        _bookmarkState.update {
            it.copy(
                url = "",
                errorMsg = "",
                isError = false
            )
        }
    }
}
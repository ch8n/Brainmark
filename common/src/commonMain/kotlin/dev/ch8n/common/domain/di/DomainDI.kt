package dev.ch8n.common.domain.di

import dev.ch8n.common.data.di.DataInjector
import dev.ch8n.common.data.remote.services.htmlParser.HtmlParserService
import dev.ch8n.common.domain.usecases.*
import io.ktor.client.*

object DomainResolver {

    private val ktorHttpClient by lazy { HttpClient() }
    fun provideHtmlParser(): HtmlParserService {
        return HtmlParserService(ktorHttpClient)
    }

    fun provideBookmarkUseCase(): BookmarkUseCases {
        val bookmarkDataSource = DataInjector.bookmarkDataSource
        return BookmarkUseCases(
            getAllBookmarkUseCase = GetAllBookmarkUseCase(bookmarkDataSource),
            getBookmarkByIdUseCase = GetBookmarkByIdUseCase(bookmarkDataSource),
            createBookmarkUseCase = CreateBookmarkUseCase(bookmarkDataSource),
            updateBookmarkUseCase = UpdateBookmarkUseCase(bookmarkDataSource),
            deleteBookmarkUseCase = DeleteBookmarkUseCase(bookmarkDataSource),
            getBookmarkByUrl = GetBookmarkByUrlUseCase(bookmarkDataSource)
        )
    }

    fun provideTagUseCase(): TagUseCases {
        val tagsDataSource = DataInjector.tagDataSource
        val getTagByIdUseCase = GetTagByIdUseCase(tagsDataSource)
        return TagUseCases(
            getAllTagsUseCase = GetAllTagsUseCase(tagsDataSource),
            getTagByIdUseCase = getTagByIdUseCase,
            getTagsByIdsUseCase = GetTagsByIdsUseCase(tagsDataSource),
            createTagUseCase = CreateTagUseCase(tagsDataSource),
            updateTagUseCase = UpdateTagUseCase(tagsDataSource, getTagByIdUseCase),
            deleteTagUseCase = DeleteTagUseCase(tagsDataSource),
        )
    }
}

object DomainInjector {

    val bookmarkUseCase by lazy {
        DomainResolver.provideBookmarkUseCase()
    }
    val tagUseCase by lazy {
        DomainResolver.provideTagUseCase()
    }
    val htmlParserService by lazy {
        DomainResolver.provideHtmlParser()
    }
}
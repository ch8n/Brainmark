package dev.ch8n.common.domain.di

import dev.ch8n.common.data.di.DataInjector
import dev.ch8n.common.data.remote.services.htmlParser.HtmlParserService
import dev.ch8n.common.data.remote.services.readerview.ReaderViewService
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
            getBookmarkByIdUseCase = GetBookmarkByIdUseCase(bookmarkDataSource),
            upsertBookmarkUseCase = UpsertBookmarkUseCase(bookmarkDataSource),
            deleteBookmarkUseCase = DeleteBookmarkUseCase(bookmarkDataSource),
            getBookmarkByUrl = GetBookmarkByUrlUseCase(bookmarkDataSource),
            getAllBookmarksPaging = GetBookmarksPaging(bookmarkDataSource),
            getBookmarksByTagPaging = GetBookmarkByTagPaging(bookmarkDataSource),
            searchAllBookmarkPaging = SearchAllBookmarkPaging(bookmarkDataSource),
            searchBookmarkByTagPaging = SearchBookmarkByTagPaging(bookmarkDataSource),
            getBookmarksByLastReadPaging = GetBookmarksByLastReadPaging(bookmarkDataSource),
            getReadingRecommendations = GetReadingRecommendations(bookmarkDataSource),
            getRevisionBookmarks = GetRevisionBookmarks(bookmarkDataSource),
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

    fun provideReaderParser() = ReaderViewService()
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

    val readerParserService by lazy {
        DomainResolver.provideReaderParser()
    }

}